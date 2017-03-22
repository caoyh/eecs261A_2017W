package yuheng.lp.solver;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.junit.Test;

import yuheng.lp.properties.MixedLinearIntergerProgrammingProp;

public class MLIPSolverTest {

	@Test
	public void mlip_brach_and_bound_test() {
		Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
		constraints.add(new LinearConstraint(new double[] { 10, 7 }, Relationship.LEQ, 40));
		constraints.add(new LinearConstraint(new double[] { 1, 1 }, Relationship.LEQ, 5));
		MixedLinearIntergerProgrammingProp mlip_prop = new MixedLinearIntergerProgrammingProp(new double[] { 17, 12 },
				0, constraints, new int[] { 1, 2 });

		MLIPSolver mlip_solver = new MLIPSolver();
		System.out.println(mlip_prop.toString());
		MLIPResult result = mlip_solver.mlip_simplex_solver_maximize(mlip_prop);
		result.print_detailed_iteration_info();
		System.out.println("Branch-and-Bound Enumeration Tree Depth:" + result.get_MLIP_enumeration_depth() + "\n");
		result.print_MLIP_enumeration_tree();
		result.print_result();
	}

}
