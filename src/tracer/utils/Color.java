package tracer.utils;

import java.util.function.Function;

import tracer.Engine.SceneFormattingException;;

/**
 * A color class capable of holding values with double floating point precision, and
 * brighter than white values. Intended for use with blending and Color
 * operations until export.
 * 
 * @author mld2443
 */
public class Color {
	public final double r, g, b;

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
	public Color(final double r, final double g, final double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Constructs a Color from a string description.
	 * 
	 * @param desc
	 *            Descriptor of the format "#FFFFFF"
	 * @throws Engine.SceneFormattingException
	 */
	public Color(final String desc) throws SceneFormattingException {
		try {
			if (desc.startsWith("#") && desc.length() == 7) {
				r = ((double) Long.parseLong(desc.substring(1, 3), 16)) / 255.0;
				g = ((double) Long.parseLong(desc.substring(3, 5), 16)) / 255.0;
				b = ((double) Long.parseLong(desc.substring(5, 7), 16)) / 255.0;
			} else
				throw new SceneFormattingException("Unknown Color format: " + desc);
		} catch (NumberFormatException e) {
			throw new SceneFormattingException("Unknown Color format: " + desc);
		}
	}

	/**
	 * Applies a transform to each individual color channel
	 * 
	 * @param transform
	 *            Function to apply
	 * @return A new transformed Color
	 */
	public Color applyTransform(Function<Double, Double> transform) {
		return new Color(transform.apply(r), transform.apply(g), transform.apply(b));
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
	 * Scales a color by multiplication. This is effectively increasing or
	 * decreasing its luminance.
	 * 
	 * @param factor
	 *            Factor by which we multiply every channel
	 * @return Scaled color
	 */
	public Color scale(final double factor) {
		return new Color(r * factor, g * factor, b * factor);
	}

	/**
	 * Reduces a color by division. This is effectively increasing or decreasing
	 * its luminance.
	 * 
	 * @param factor
	 *            Factor by which we divide every channel
	 * @return Reduced color
	 */
	public Color reduce(final double factor) {
		return new Color(r / factor, g / factor, b / factor);
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
		return new Color(c1.r * c2.r, c1.g * c2.g, c1.b * c2.b);
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
	public static Color linearBlend(final Color c1, final Color c2, final double interpolation) {
		return add(c1.scale(1.0 - interpolation), c2.scale(interpolation));
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
		return new Color(0.7, 0.7, 0.7);
	}

	/**
	 * Predefined dark grey color.
	 * 
	 * @return #4C4C4C
	 */
	public static Color darkGrey() {
		return new Color(0.3, 0.3, 0.3);
	}

	/**
	 * Predefined black color.
	 * 
	 * @return #000000
	 */
	public static Color black() {
		return new Color(0, 0, 0);
	}

	@Override
	public String toString() {
		return String.format("#%06X", quantize());
	}
}
