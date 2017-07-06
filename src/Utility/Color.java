package Utility;

public class Color {
	float r,g,b;
	
	Color(final float r, final float g, final float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void scale(final float scale) {
		r *= scale;
		g *= scale;
		b *= scale;
	}
	
	public void add(final Color c) {
		r += c.r;
		g += c.g;
		b += c.b;
	}
	
	public void mix(final Color c) {
		r *= c.r;
		g *= c.g;
		b *= c.b;
	}
	
	public int quantize() {
		int r256 = Math.min(Math.max((int)(r * 255), 0),255);
		int g256 = Math.min(Math.max((int)(g * 255), 0),255);
		int b256 = Math.min(Math.max((int)(b * 255), 0),255);
		
		return r256 << 16 | g256 << 8 | b256;
	}
	
	//MARK: Predefined colors
	
	public static Color red() {
		return new Color(1,0,0);
	}
	public static Color green() {
		return new Color(1,0,0);
	}
	public static Color blue() {
		return new Color(1,0,0);
	}
	public static Color yellow() {
		return new Color(1,1,0);
	}
	public static Color magenta() {
		return new Color(1,0,1);
	}
	public static Color cyan() {
		return new Color(0,1,1);
	}
	public static Color white() {
		return new Color(1,1,1);
	}
	public static Color lightGrey() {
		return new Color(0.7f,0.7f,0.7f);
	}
	public static Color darkGrey() {
		return new Color(0.3f,0.3f,0.3f);
	}
	public static Color black() {
		return new Color(0,0,0);
	}
}
