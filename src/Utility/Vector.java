package Utility;

public class Vector {
	float x,y,z;
	
	Vector(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float magnitude() {
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	
	public static Vector add(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z);
	}
	
	public static Vector sub(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z);
	}

	public static Vector inlineMultiply(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z);
	}
	
	public static float dot(final Vector lhs, final Vector rhs) {
		return lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;
	}
	
	public static Vector cross(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.y * rhs.z - lhs.z * rhs.y,
						  lhs.z * rhs.x - lhs.x * rhs.z,
						  lhs.x * rhs.y - lhs.y * rhs.x);
	}
}
