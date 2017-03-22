package yuheng.lp.solver;

import org.apache.commons.math3.optim.PointValuePair;

public class LPOptimizationResult {

	protected PointValuePair solution;
	protected LPOptimizationStatus status;

	public LPOptimizationResult(PointValuePair solution, LPOptimizationStatus status) {
		this.solution = solution;
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if (this.status.equals(LPOptimizationStatus.OPTIMAL)) {
			str.append("Optimal Value:\n" + this.solution.getValue() + "\nValue of Decision Variable:\n{");
			int length = this.solution.getPoint().length;
			for (int i = 0; i < length; i++) {
				if (i < length - 1)
					str.append(this.solution.getPoint()[i] + "; ");
				else
					str.append(this.solution.getPoint()[i] + "}\n");
			}
		} else {
			str.append("The Linear Programming is " + this.status.toString() + ".\n");
		}
		return str.toString();
	}

	public PointValuePair getSolution() {
		return solution;
	}

	public LPOptimizationStatus getStatus() {
		return status;
	}
}
