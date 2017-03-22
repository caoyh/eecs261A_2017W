package yuheng.lp.properties;

import java.util.Collection;

import org.apache.commons.math3.optim.linear.LinearConstraint;

public class MixedLinearIntergerProgrammingProp extends LinearProgrammingProp {
	private int[] integer_variables;

	public MixedLinearIntergerProgrammingProp(double[] obj_coefficients, double obj_constantTerm,
			int[] integer_variables) {
		super(obj_coefficients, obj_constantTerm);
		this.integer_variables = integer_variables;
	}

	public MixedLinearIntergerProgrammingProp(double[] obj_coefficients, double obj_constantTerm,
			Collection<LinearConstraint> constraints, int[] integer_variables) {
		super(obj_coefficients, obj_constantTerm, constraints);
		this.integer_variables = integer_variables;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		str.append("Integer Indices:\n{");
		for (int i = 0; i < integer_variables.length; i++) {
			if (i == integer_variables.length - 1)
				str.append(integer_variables[i] + "}\n");
			else
				str.append(integer_variables[i] + "; ");
		}
		return str.toString();
	}

	public int[] getInteger_variables() {
		return integer_variables;
	}

	public void setInteger_variables(int[] integer_variables) {
		this.integer_variables = integer_variables;
	}

}
