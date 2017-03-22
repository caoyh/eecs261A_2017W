package yuheng.lp.solver;

public enum MLIPTermination {
	CONTINUE("continue"), CANDIDATE("candidate"), INFEASIBLE("infeasible"), OPTIMAL("optimal"), PRUNING(
			"pruning"), UNBOUNDED("unbounded"), EXCEED("exceed"), SUSPEND("suspend");

	private final String stringValue;

	MLIPTermination(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		return stringValue;
	}
}
