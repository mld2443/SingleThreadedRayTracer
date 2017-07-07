package Utility;

/**
 * A Range holds a lower and upper bound, and a function to test whether a value
 * is inside those bounds
 * 
 * @author mld2443
 *
 * @param <T>
 *            extends Comparable
 */
public class Range<T extends Comparable<? super T>> {
	public final T lower, upper;

	/**
	 * Constructor to define a new Range
	 * @param lower bound
	 * @param upper bound
	 */
	public Range(final T lower, final T upper) {
		this.lower = lower;
		this.upper = upper;
	}

	/**
	 * The primary function of a Range, this can test whether a value is inside
	 * it.
	 * 
	 * @param value
	 * @return true if value is inside the bounds of this range, false if
	 *         outside
	 */
	public boolean contains(final T value) {
		if (value.compareTo(upper) > 0)
			return false;
		if (value.compareTo(lower) < 0)
			return false;
		return true;
	}
}
