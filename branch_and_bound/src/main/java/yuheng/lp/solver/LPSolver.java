package yuheng.lp.solver;

import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.linear.UnboundedSolutionException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import yuheng.lp.properties.LinearProgrammingProp;

public class LPSolver {
	public static LPOptimizationResult lp_simplex_solver_maximize(LinearProgrammingProp lp_prop) {
		LPOptimizationResult result = null;
		PointValuePair solution = null;
		try {
			solution = new SimplexSolver().optimize(lp_prop.getF(), new LinearConstraintSet(lp_prop.getConstraints()),
					new NonNegativeConstraint(true), GoalType.MAXIMIZE);
		} catch (UnboundedSolutionException ubounded_exception) {
			result = new LPOptimizationResult(null, LPOptimizationStatus.UNBOUNDED);
		} catch (NoFeasibleSolutionException infeasible_exception) {
			result = new LPOptimizationResult(null, LPOptimizationStatus.INFEASIBLE);
		} catch (TooManyIterationsException exceed_max_iteration_exception) {
			result = new LPOptimizationResult(null, LPOptimizationStatus.EXCEED);
		}

		if (solution != null) {
			result = new LPOptimizationResult(solution, LPOptimizationStatus.OPTIMAL);
		}

		return result;
	}
}
