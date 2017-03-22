package yuheng.lp.branch_and_bound;

public enum MixlpBBMode {
	CONSOLE("console");

	private final String stringValue;

	MixlpBBMode(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public String toString() {
		return stringValue;
	}
}
