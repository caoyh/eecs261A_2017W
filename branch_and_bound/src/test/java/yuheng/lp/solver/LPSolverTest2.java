package yuheng.lp.solver;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.junit.Test;

import yuheng.lp.properties.LinearProgrammingProp;

public class LPSolverTest2 {

	@Test
	public void lp_simplex_solver_maximize_optimal_test() {
		/**
		 * Exercise 12.2 test, to use it, set LPSolver Nonnegative constraint to
		 * false
		 */
		System.out.println("Test of solving linear programming via simplex method in maximize form.\n"
				+ "This test will also test the correctness of LinearProgrammingProp constructor and printing.\n");

		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		constraints.add(new LinearConstraint(new double[] { 0, 1, -1, 0, 0, 0 }, Relationship.LEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 1, 1, 0, -1, 0, 0 }, Relationship.LEQ, 3));
		constraints.add(new LinearConstraint(new double[] { 2, 1, 0, 0, -1, 0 }, Relationship.LEQ, 1));
		constraints.add(new LinearConstraint(new double[] { 4, 1, 0, 0, 0, -1 }, Relationship.LEQ, 2));
		constraints.add(new LinearConstraint(new double[] { 0, -1, -1, 0, 0, 0 }, Relationship.LEQ, 0));
		constraints.add(new LinearConstraint(new double[] { -1, -1, 0, -1, 0, 0 }, Relationship.LEQ, -3));
		constraints.add(new LinearConstraint(new double[] { -2, -1, 0, 0, -1, 0 }, Relationship.LEQ, -1));
		constraints.add(new LinearConstraint(new double[] { -4, -1, 0, 0, 0, -1 }, Relationship.LEQ, -2));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 1, 0, 0, 0 }, Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 1, 0, 0 }, Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 0, 1, 0 }, Relationship.GEQ, 0));
		constraints.add(new LinearConstraint(new double[] { 0, 0, 0, 0, 0, 1 }, Relationship.GEQ, 0));
		LinearProgrammingProp lp_prop = new LinearProgrammingProp(new double[] { 0, 0, -1, -1, -1, -1 }, 0,
				constraints);

		LPOptimizationResult result = LPSolver.lp_simplex_solver_maximize(lp_prop);
		System.out.println(result.toString());
	}
}
