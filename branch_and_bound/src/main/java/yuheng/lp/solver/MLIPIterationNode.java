package yuheng.lp.solver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

public class MLIPIterationNode {
	private int current_iteration;
	private LPOptimizationResult lp_result;
	private MLIPTermination termination;
	private MLIPIterationNode left_child = null;
	private MLIPIterationNode right_child = null;
	private Collection<LinearConstraint> addon_constraints = null;
	private double current_optimal_value;
	private DecimalFormat df;

	public MLIPIterationNode(int current_iteration, LPOptimizationResult lp_result,
			Collection<LinearConstraint> addon_constraints) {
		this.current_iteration = current_iteration;
		this.lp_result = lp_result;
		this.addon_constraints = addon_constraints;
		this.current_optimal_value = Double.NaN;
		df = new DecimalFormat("#0.000");
	}

	public void addConstraint(double[] cons_coefficients, Relationship relationship, double value) {
		if (this.addon_constraints == null)
			this.addon_constraints = new ArrayList<LinearConstraint>();
		this.addon_constraints.add(new LinearConstraint(cons_coefficients, relationship, value));
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Current Iteration:\nP" + this.getCurrent_iteration() + "\nCurrent Optimal Value:\n"
				+ this.getCurrent_optimal_value() + "\n");

		if (this.termination == MLIPTermination.SUSPEND) {
			str.append("My termination status:" + this.termination.toString() + ".\n"
					+ "The current iteration is terminated by others' exceed/ubounded iteration.");
		}

		if (this.termination.equals(MLIPTermination.UNBOUNDED) || this.termination.equals(MLIPTermination.EXCEED)) {
			str.append("My termination status:" + this.termination.toString() + ".\n");
			return str.toString();
		}

		if (this.addon_constraints != null) {
			str.append("The extra constraints of this iteration: \n");
			for (LinearConstraint cons : this.addon_constraints)
				str.append(cons.getCoefficients() + " " + cons.getRelationship().toString() + " " + cons.getValue()
						+ "\n");
		}

		if (this.termination.equals(MLIPTermination.INFEASIBLE)) {
			str.append("My termination status:" + this.termination.toString() + ".\n");
			return str.toString();
		}

		str.append("My termination status:" + this.termination + "\nMy Optimal Value:\n"
				+ df.format(this.lp_result.getSolution().getValue()) + "\nValue of Decision Variable:\n{");
		int length = this.getLp_result().getSolution().getPoint().length;
		for (int i = 0; i < length; i++) {
			if (i < length - 1)
				str.append(df.format(this.getLp_result().getSolution().getPoint()[i]) + "; ");
			else
				str.append(df.format(this.getLp_result().getSolution().getPoint()[i]) + "}\n");
		}

		return str.toString();
	}

	public int getCurrent_iteration() {
		return current_iteration;
	}

	public LPOptimizationResult getLp_result() {
		return lp_result;
	}

	public MLIPTermination getTermination() {
		return termination;
	}

	public void setTermination(MLIPTermination termination) {
		this.termination = termination;
	}

	public MLIPIterationNode getLeft_child() {
		return left_child;
	}

	public void setLeft_child(MLIPIterationNode left_child) {
		this.left_child = left_child;
	}

	public MLIPIterationNode getRight_child() {
		return right_child;
	}

	public void setRight_child(MLIPIterationNode right_child) {
		this.right_child = right_child;
	}

	public Collection<LinearConstraint> getAddon_constraints() {
		return addon_constraints;
	}

	public double getCurrent_optimal_value() {
		return current_optimal_value;
	}

	public void setCurrent_optimal_value(double current_optimal_value) {
		this.current_optimal_value = current_optimal_value;
	}

}
