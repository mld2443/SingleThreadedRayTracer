package utilities;

/**
 * A color class capable of holding values with floating point precision, and
 * brighter than white values. Intended for use with blending and Color
 * operations until export.
 * 
 * @author mld2443
 */
public class Color {
	public final float r, g, b;

	/**
	 * Constructs a color with specified red, green and blue values.
	 * 
	 * @param r
	 *            Red brightness
	 * @param g
	 *            Green brightness
	 * @param b
	 *            Blue brightness
	 */
	public Color(final float r, final float g, final float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Export of a color to an integer. This reduces each channel to an 8-bit
	 * value, clipping the values and then quantizing them. After which no more
	 * operations may be performed on the color.
	 * 
	 * @return An integer containing the binary representation of the Color
	 */
	public int quantize() {
		int r256 = Math.min(Math.max((int) (r * 255), 0), 255);
		int g256 = Math.min(Math.max((int) (g * 255), 0), 255);
		int b256 = Math.min(Math.max((int) (b * 255), 0), 255);

		return r256 << 16 | g256 << 8 | b256;
	}

	////////////////
	// Operations //
	////////////////

	/**
	 * Scales a color. This is effectively increasing or decreasing its
	 * luminance.
	 * 
	 * @param scale
	 *            Factor by which we multiply every channel
	 * @return Scaled color
	 */
	public Color scale(final float scale) {
		return new Color(r * scale, g * scale, b * scale);
	}

	/**
	 * Add one color to another with their respective color channels.
	 * 
	 * @param c1
	 *            Color
	 * @param c2
	 *            Color
	 * @return Sum of the two colors
	 */
	public static Color add(final Color c1, final Color c2) {
		return new Color(c1.r + c2.r, c1.g + c2.g, c1.b + c2.b);
	}

	/**
	 * Mix two colors by multiplication of their respective color channels.
	 * 
	 * @param c1
	 *            Color
	 * @param c2
	 *            Color
	 * @return Product of the two colors
	 */
	public static Color mix(final Color c1, final Color c2) {
		return new Color(c1.r + c2.r, c1.g + c2.g, c1.b + c2.b);
	}

	/**
	 * Linearly interpolate between two colors.
	 * 
	 * @param c1
	 *            Color returned if interpolation is 0.0
	 * @param c2
	 *            Color returned if interpolation is 1.0
	 * @param interpolation
	 *            Amount of how much of each color the result will be
	 * @return A linear interpolation between the two colors
	 */
	public static Color linearBlend(final Color c1, final Color c2, final float interpolation) {
		return add(c1.scale(1.0f - interpolation), c2.scale(interpolation));
	}

	///////////////////////
	// Predefined colors //
	///////////////////////

	/**
	 * Predefined red color.
	 * 
	 * @return #FF0000
	 */
	public static Color red() {
		return new Color(1, 0, 0);
	}

	/**
	 * Predefined green color.
	 * 
	 * @return #00FF00
	 */
	public static Color green() {
		return new Color(0, 1, 0);
	}

	/**
	 * Predefined blue color.
	 * 
	 * @return #0000FF
	 */
	public static Color blue() {
		return new Color(0, 0, 1);
	}

	/**
	 * Predefined yellow color.
	 * 
	 * @return #FFFF00
	 */
	public static Color yellow() {
		return new Color(1, 1, 0);
	}

	/**
	 * Predefined magenta color.
	 * 
	 * @return #FF00FF
	 */
	public static Color magenta() {
		return new Color(1, 0, 1);
	}

	/**
	 * Predefined cyan color.
	 * 
	 * @return #00FFFF
	 */
	public static Color cyan() {
		return new Color(0, 1, 1);
	}

	/**
	 * Predefined white color.
	 * 
	 * @return #FFFFFF
	 */
	public static Color white() {
		return new Color(1, 1, 1);
	}

	/**
	 * Predefined light grey color.
	 * 
	 * @return #B2B2B2
	 */
	public static Color lightGrey() {
		return new Color(0.7f, 0.7f, 0.7f);
	}

	/**
	 * Predefined dark grey color.
	 * 
	 * @return #4C4C4C
	 */
	public static Color darkGrey() {
		return new Color(0.3f, 0.3f, 0.3f);
	}

	/**
	 * Predefined black color.
	 * 
	 * @return #000000
	 */
	public static Color black() {
		return new Color(0, 0, 0);
	}
}
