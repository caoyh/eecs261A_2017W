package yuheng.lp.properties;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;

public class LinearProgrammingProp {
	private LinearObjectiveFunction f;
	private Collection<LinearConstraint> constraints;

	public LinearProgrammingProp(double[] obj_coefficients, double obj_constantTerm) {
		this.f = new LinearObjectiveFunction(obj_coefficients, obj_constantTerm);
		constraints = new ArrayList<LinearConstraint>();
	}

	public LinearProgrammingProp(double[] obj_coefficients, double obj_constantTerm,
			Collection<LinearConstraint> constraints) {
		this.f = new LinearObjectiveFunction(obj_coefficients, obj_constantTerm);
		this.constraints = constraints;
	}

	public void addConstraint(double[] cons_coefficients, Relationship relationship, double value) {
		if (this.constraints == null)
			constraints = new ArrayList<LinearConstraint>();
		this.constraints.add(new LinearConstraint(cons_coefficients, relationship, value));
	}

	public void addConstraints(Collection<LinearConstraint> constraints) {
		if (constraints == null)
			return;
		if (this.constraints == null)
			this.constraints = new ArrayList<LinearConstraint>();
		this.constraints.addAll(constraints);
	}

	public void removeConstraint(double[] cons_coefficients, Relationship relationship, double value) {
		if (this.constraints != null)
			this.constraints.remove(new LinearConstraint(cons_coefficients, relationship, value));
	}

	public void removeConstraints(Collection<LinearConstraint> constraints) {
		if (this.constraints != null)
			this.constraints.removeAll(constraints);
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Objective Function Coefficients:\n" + this.f.getCoefficients().toString()
				+ "\nObjective Function Constant Term:\n" + this.f.getConstantTerm() + "\nConstraits:\n");

		for (LinearConstraint cons : this.constraints)
			str.append(cons.getCoefficients() + " " + cons.getRelationship().toString() + " " + cons.getValue() + "\n");

		return str.toString();
	}

	public LinearObjectiveFunction getF() {
		return f;
	}

	public void setF(LinearObjectiveFunction f) {
		this.f = f;
	}

	public Collection<LinearConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(Collection<LinearConstraint> constraints) {
		this.constraints = constraints;
	}
}
