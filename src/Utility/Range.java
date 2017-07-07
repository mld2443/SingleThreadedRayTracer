package Utility;

public class Range {
	public final float lower, upper;

	public Range(final float lower, final float upper) {
		this.lower = lower;
		this.upper = upper;
	}

	public boolean isInside(final float value) {
		if (value > upper)
			return false;
		if (value < lower)
			return false;
		return true;
	}
}
