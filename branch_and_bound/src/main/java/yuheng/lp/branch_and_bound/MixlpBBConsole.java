package yuheng.lp.branch_and_bound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.Relationship;

import yuheng.lp.properties.MixedLinearIntergerProgrammingProp;
import yuheng.lp.solver.MLIPResult;
import yuheng.lp.solver.MLIPSolver;

public class MixlpBBConsole {

	public void mixed_lp_branch_and_bound(MixlpBBMode mode, String[] args) {
		switch (mode) {
		case CONSOLE:
			run_console_mode();
			break;
		default:
			System.out.println("Invalid mode! Exit!");
			break;

		}
	}

	private void run_console_mode() {
		Scanner sc = new Scanner(System.in);
		boolean conti = true;
		while (conti) {
			System.out.println("The console mode only accpet maximun standard formulation in the format of:\n"
					+ "max c'x\n" + "S.T. Ax <= b\n" + "x >= 0\n" + "xi ∈ Z\n");

			System.out.println("---------------------------------------------------------------");
			System.out.println(
					"\nInput objective function coefficients vector c (separate each element by a single space, e.g.: c1 c2 )."
							+ "\nPress enter to finish:");
			String vector_c = sc.nextLine();

			System.out.println(
					"\nInput constraint coefficients maxtrix A (separate each element by a single space and separe each row by \",\", e.g.: a11 a12,a21 a22 )."
							+ "\nPress enter to finish:");
			String matrix_A = sc.nextLine();

			System.out.println(
					"\nInput constraint values vector b (separate each element by a single space, e.g.: b1 b2 )."
							+ "\nPress enter to finish:");
			String vector_b = sc.nextLine();

			System.out.println(
					"\nInput indices associated with the integer variables (separate each element by a single space, e.g.: x1 x2 )."
							+ "\nPress enter to finish:");
			String integer_indice_vetor = sc.nextLine();
			System.out.println("---------------------------------------------------------------");

			Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
			String[] constraint_vetors = matrix_A.split(",");
			String[] constraint_values = vector_b.split(" ");

			if (constraint_vetors.length != constraint_values.length) {
				System.out.println("Constraint number not match! Exit!");
				sc.close();
				return;
			}

			String[] obj_coefficients = vector_c.split(" ");
			String[] integer_variables = integer_indice_vetor.split(" ");

			if (integer_variables.length > obj_coefficients.length) {
				System.out.println("Too many integer variable is indicated! Exit!");
				sc.close();
				return;
			}

			for (int i = 0; i < constraint_vetors.length; i++) {
				String[] curr_cons_coeff = constraint_vetors[i].split(" ");
				if (obj_coefficients.length != curr_cons_coeff.length) {
					System.out.println("Decision variable number not match! Exit!");
					sc.close();
					return;
				}

				double[] cons_coeff_double = new double[curr_cons_coeff.length];
				for (int j = 0; j < curr_cons_coeff.length; j++) {
					cons_coeff_double[j] = Double.parseDouble(curr_cons_coeff[j]);
				}

				constraints.add(new LinearConstraint(cons_coeff_double, Relationship.LEQ,
						Double.parseDouble(constraint_values[i])));
			}

			double[] obj_coeff_double = new double[obj_coefficients.length];
			for (int i = 0; i < obj_coefficients.length; i++)
				obj_coeff_double[i] = Double.parseDouble(obj_coefficients[i]);

			int[] integer_indices = new int[integer_variables.length];
			for (int i = 0; i < integer_variables.length; i++)
				integer_indices[i] = Integer.parseInt(integer_variables[i]);

			MixedLinearIntergerProgrammingProp mlip_prop = new MixedLinearIntergerProgrammingProp(obj_coeff_double, 0,
					constraints, integer_indices);
			MLIPSolver mlip_solver = new MLIPSolver();
			System.out.println("\nThe MLIP is:\n" + mlip_prop.toString());

			MLIPResult result = mlip_solver.mlip_simplex_solver_maximize(mlip_prop);
			System.out.println("---------------------------------------------------------------");
			result.print_result();
			System.out.println("---------------------------------------------------------------");
			System.out.println("Enumeartion Tree:");
			result.print_MLIP_enumeration_tree();
			System.out.println("---------------------------------------------------------------");
			System.out.println("Detailed Iteration Info:");
			result.print_detailed_iteration_info();

			System.out.println("---------------------------------------------------------------");
			System.out.println("\nSolve another Mixed Linear Integer Programming problem?(Y/N):");
			switch (sc.nextLine()) {
			case "Y":
				break;
			case "N":
				conti = false;
				break;
			default:
				System.out.println("Invalid Input! Exit");
				sc.close();
				return;
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		MixlpBBConsole mixlp = new MixlpBBConsole();
		mixlp.mixed_lp_branch_and_bound(MixlpBBMode.CONSOLE, args);
	}

}
