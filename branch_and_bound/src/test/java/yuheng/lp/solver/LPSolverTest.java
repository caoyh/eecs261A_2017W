package yuheng.lp.solver;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.junit.Test;

import yuheng.lp.properties.LinearProgrammingProp;

public class LPSolverTest {

	@Test
	public void lp_simplex_solver_maximize_optimal_test() {
		/**
		 * LinearProgrammingProp Constructor Method 1 and Optimal Test
		 */
		System.out.println("Test of solving linear programming via simplex method in maximize form.\n"
				+ "This test will also test the correctness of LinearProgrammingProp constructor and printing.\n");

		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		constraints.add(new LinearConstraint(new double[] { 2, -5, 1 }, Relationship.LEQ, -5));
		constraints.add(new LinearConstraint(new double[] { 2, -1, 2 }, Relationship.LEQ, 4));
		LinearProgrammingProp lp_prop = new LinearProgrammingProp(new double[] { -1, -3, -1 }, 0, constraints);

		System.out.println("LinearProgrammingProp Constructor Method 1 correct.\n\n"
				+ "Valid if the solver can calculate optimal value.\n" + "The linear programming is:\n"
				+ lp_prop.toString());

		LPOptimizationResult result = LPSolver.lp_simplex_solver_maximize(lp_prop);
		System.out.println(result.toString());

		/**
		 * LinearProgrammingProp Objective Function/Constraints Reset and
		 * Infeasible Test
		 */
		lp_prop.setF(new LinearObjectiveFunction(new double[] { 1, 3 }, 0));
		lp_prop.removeConstraints(constraints);
		constraints.clear();
		constraints.add(new LinearConstraint(new double[] { -1, -1 }, Relationship.LEQ, -3));
		constraints.add(new LinearConstraint(new double[] { -1, 1 }, Relationship.LEQ, -1));
		constraints.add(new LinearConstraint(new double[] { 1, 2 }, Relationship.LEQ, 2));
		lp_prop.addConstraints(constraints);

		assert (constraints.equals(lp_prop.getConstraints()));

		System.out.println("LinearProgrammingProp Constraints addALL/removeAll method correct.\n\n"
				+ "Valid if the solver can detect infeasible.\n" + "The linear programming is:\n" + lp_prop.toString());

		result = LPSolver.lp_simplex_solver_maximize(lp_prop);
		System.out.println(result.toString());

		/**
		 * LinearProgrammingProp Constructor Method 2 Unboundedness Test
		 */
		lp_prop = new LinearProgrammingProp(new double[] { -1, 0, 1 }, 5);
		lp_prop.addConstraint(new double[] { -3, 0, 2 }, Relationship.LEQ, 5);
		lp_prop.addConstraint(new double[] { -4, 0, 0 }, Relationship.LEQ, 7);

		System.out.println("LinearProgrammingProp Constructor Method 2 correct.\n\n"
				+ "Valid if the solver can detect unboundedness.\n" + "The linear programming is:\n"
				+ lp_prop.toString());

		result = LPSolver.lp_simplex_solver_maximize(lp_prop);
		System.out.println(result.toString());
	}
}
