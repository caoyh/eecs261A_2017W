package yuheng.lp.solver;

public enum LPOptimizationStatus {
	OPTIMAL("optimal"), INFEASIBLE("infeasible"), UNBOUNDED("unbounded"), EXCEED("exceed");

	private final String stringValue;

	LPOptimizationStatus(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		return stringValue;
	}
}
