package yuheng.lp.solver;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import yuheng.lp.properties.MixedLinearIntergerProgrammingProp;

public class MLIPSolver {
	public MLIPResult mlip_simplex_solver_maximize(MixedLinearIntergerProgrammingProp mlip_prop) {
		IteartionStatus iterationStatus = new IteartionStatus();
		MLIPIterationNode root = doIteration(mlip_prop, iterationStatus, null);
		return new MLIPResult(root);
	}

	private MLIPIterationNode doIteration(MixedLinearIntergerProgrammingProp my_mlip_prop,
			IteartionStatus iterationStatus, Collection<LinearConstraint> addon_constraints) {

		Collection<LinearConstraint> my_addon_constraints = null;
		if (addon_constraints != null) {
			my_addon_constraints = new ArrayList<LinearConstraint>();
			my_addon_constraints.addAll(addon_constraints);
		}

		MLIPIterationNode current_iteration_result = null;
		if (iterationStatus.getCurrent_optimal() != null) {
			switch (iterationStatus.getCurrent_optimal().getTermination()) {
			case EXCEED:
				/**
				 * Exit since there is an iteration exceed maximum.
				 */
				current_iteration_result = new MLIPIterationNode(iterationStatus.getCurrent_iteration(), null,
						my_addon_constraints);
				iterationStatus.iteration_increase();
				current_iteration_result.setTermination(MLIPTermination.SUSPEND);
				return current_iteration_result;
			case UNBOUNDED:
				/**
				 * Exit since there exists an unbounded iteration.
				 */
				current_iteration_result = new MLIPIterationNode(iterationStatus.getCurrent_iteration(), null,
						my_addon_constraints);
				iterationStatus.iteration_increase();
				current_iteration_result.setTermination(MLIPTermination.SUSPEND);
				return current_iteration_result;
			case CANDIDATE:
				/**
				 * Terminate whole branch if current optimal value of LP is
				 * smaller than the candidate. Otherwise, follow the original
				 * procedure.
				 */
				my_mlip_prop.addConstraints(my_addon_constraints);
				LPOptimizationResult lp_result = LPSolver.lp_simplex_solver_maximize(my_mlip_prop);
				current_iteration_result = new MLIPIterationNode(iterationStatus.getCurrent_iteration(), lp_result,
						my_addon_constraints);

				if (lp_result.getStatus().equals(LPOptimizationStatus.OPTIMAL)) {
					if (lp_result.getSolution().getValue() <= iterationStatus.getCurrent_optimal().getLp_result()
							.getSolution().getValue()) {
						current_iteration_result.setTermination(MLIPTermination.PRUNING);
						current_iteration_result.setCurrent_optimal_value(
								iterationStatus.getCurrent_optimal().getCurrent_optimal_value());
						iterationStatus.iteration_increase();
						return current_iteration_result;
					}
				}
				break;
			default:
				/**
				 * Just two case if we have a not NULL current optimal
				 * iteration.
				 */
				break;

			}
		}

		/**
		 * Calculate current iteration, check its termination, create child
		 * iteration if it should continue.
		 */

		if (current_iteration_result == null) {
			my_mlip_prop.addConstraints(my_addon_constraints);
			LPOptimizationResult lp_result = LPSolver.lp_simplex_solver_maximize(my_mlip_prop);
			current_iteration_result = new MLIPIterationNode(iterationStatus.getCurrent_iteration(), lp_result,
					my_addon_constraints);
		}

		iterationStatus.iteration_increase();

		/**
		 * Terminate the current iteration if it exceed the maximum iterations.
		 */
		if (current_iteration_result.getLp_result().getStatus().equals(LPOptimizationStatus.EXCEED)) {
			current_iteration_result.setTermination(MLIPTermination.EXCEED);
			iterationStatus.setCurrent_optimal(current_iteration_result);
			return current_iteration_result;
		}

		/**
		 * Terminate the current iteration if it's unbounded.
		 */
		if (current_iteration_result.getLp_result().getStatus().equals(LPOptimizationStatus.UNBOUNDED)) {
			current_iteration_result.setTermination(MLIPTermination.UNBOUNDED);
			iterationStatus.setCurrent_optimal(current_iteration_result);
			return current_iteration_result;
		}

		/**
		 * Terminate the current iteration if it's infeasible.
		 */
		if (current_iteration_result.getLp_result().getStatus().equals(LPOptimizationStatus.INFEASIBLE)) {
			current_iteration_result.setTermination(MLIPTermination.INFEASIBLE);
			if (iterationStatus.getCurrent_optimal() != null)
				current_iteration_result
						.setCurrent_optimal_value(iterationStatus.getCurrent_optimal().getCurrent_optimal_value());
			return current_iteration_result;
		}

		/**
		 * Check if the current solution is a candidate solution.
		 */

		int[] integer_variables = my_mlip_prop.getInteger_variables();
		boolean candidate = true;
		for (int i = 0; i < integer_variables.length; i++) {
			double var = current_iteration_result.getLp_result().getSolution().getPoint()[integer_variables[i] - 1];
			if (Math.floor(var) < var) {
				candidate = false;
				current_iteration_result.setTermination(MLIPTermination.CONTINUE);

				if (iterationStatus.getCurrent_optimal() != null)
					current_iteration_result
							.setCurrent_optimal_value(iterationStatus.getCurrent_optimal().getCurrent_optimal_value());
				// create new constraint coefficients
				RealVector new_constraint_coefficients = new ArrayRealVector(
						current_iteration_result.getLp_result().getSolution().getPoint().length);
				new_constraint_coefficients.setEntry(integer_variables[i] - 1, 1);

				if (my_addon_constraints == null)
					my_addon_constraints = new ArrayList<LinearConstraint>();
				// Left Branch
				LinearConstraint left_branch_cons = new LinearConstraint(new_constraint_coefficients, Relationship.LEQ,
						Math.floor(var));
				my_addon_constraints.add(left_branch_cons);
				current_iteration_result
						.setLeft_child(doIteration(my_mlip_prop, iterationStatus, my_addon_constraints));
				my_addon_constraints.remove(left_branch_cons);

				// Right Branch
				LinearConstraint right_branch_cons = new LinearConstraint(new_constraint_coefficients, Relationship.GEQ,
						Math.ceil(var));
				my_addon_constraints.add(right_branch_cons);
				current_iteration_result
						.setRight_child(doIteration(my_mlip_prop, iterationStatus, my_addon_constraints));
				my_addon_constraints.remove(right_branch_cons);
				break;
			}

		}

		if (candidate) {
			current_iteration_result.setTermination(MLIPTermination.CANDIDATE);
			if (iterationStatus.getCurrent_optimal() != null) {
				if (current_iteration_result.getLp_result().getSolution().getValue() > iterationStatus
						.getCurrent_optimal().getLp_result().getSolution().getValue()) {
					iterationStatus.getCurrent_optimal().setTermination(MLIPTermination.CANDIDATE);
					current_iteration_result.setTermination(MLIPTermination.OPTIMAL);
					current_iteration_result
							.setCurrent_optimal_value(current_iteration_result.getLp_result().getSolution().getValue());
					iterationStatus.setCurrent_optimal(current_iteration_result);
				} else
					current_iteration_result
							.setCurrent_optimal_value(iterationStatus.getCurrent_optimal().getCurrent_optimal_value());
			} else {
				current_iteration_result.setTermination(MLIPTermination.OPTIMAL);
				current_iteration_result
						.setCurrent_optimal_value(current_iteration_result.getLp_result().getSolution().getValue());
				iterationStatus.setCurrent_optimal(current_iteration_result);
			}
		}

		/**
		 * Pop add on constraints
		 */
		if (my_mlip_prop.getConstraints() != null && my_addon_constraints != null)
			my_mlip_prop.removeConstraints(my_addon_constraints);
		return current_iteration_result;
	}

	private class IteartionStatus {
		private int current_iteration;
		private MLIPIterationNode current_optimal;

		private IteartionStatus() {
			setCurrent_iteration(0);
			setCurrent_optimal(null);
		}

		private void iteration_increase() {
			this.current_iteration++;
		}

		private int getCurrent_iteration() {
			return current_iteration;
		}

		private void setCurrent_iteration(int current_iteration) {
			this.current_iteration = current_iteration;
		}

		private MLIPIterationNode getCurrent_optimal() {
			return current_optimal;
		}

		private void setCurrent_optimal(MLIPIterationNode current_optimal) {
			this.current_optimal = current_optimal;
		}

	}
}
