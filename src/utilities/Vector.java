package utilities;

import java.util.Random;

/**
 * A mathematical vector representing either a point or direction in
 * 3-Dimensional space.
 * 
 * @author mld2443
 */
public final class Vector {
	// TODO: determine speed difference if using doubles
	public final float x, y, z;
	private final static Random gen = new Random();

	/**
	 * Default constructor which creates <0,0,0>
	 */
	public Vector() {
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}

	/**
	 * Primary constructor to make a new Vector <x,y,z>
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @return the length of the vector
	 */
	public float magnitude() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * @return a unit vector with the same direction
	 */
	public Vector normalize() {
		return div(this, magnitude());
	}

	/**
	 * @return a randomly generated vector with a length of 1.0
	 */
	public static Vector randomInUnitSphere() {
		return new Vector((float) gen.nextGaussian(), (float) gen.nextGaussian(), (float) gen.nextGaussian())
				.normalize();
	}

	////////////////
	// OPERATIONS //
	////////////////
	/**
	 * Vector inversion/negation. Reverses the direction of the vector without
	 * changing its magnitude.
	 * 
	 * @return Inverted vector
	 */
	public Vector invert() {
		return new Vector(-x, -y, -z);
	}

	/**
	 * Vector addition
	 * 
	 * @param lhs
	 * @param rhs
	 * @return vector sum
	 */
	public static Vector add(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.x + rhs.x, lhs.y + rhs.y, lhs.z + rhs.z);
	}

	/**
	 * Vector subtraction
	 * 
	 * @param lhs
	 * @param rhs
	 * @return vector difference
	 */
	public static Vector sub(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.x - rhs.x, lhs.y - rhs.y, lhs.z - rhs.z);
	}

	/**
	 * Element-wise multiplication
	 * 
	 * @param lhs
	 * @param rhs
	 * @return vector with each element product
	 */
	public static Vector inlineMultiply(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.x * rhs.x, lhs.y * rhs.y, lhs.z * rhs.z);
	}

	/**
	 * Scale a vector by division
	 * 
	 * @param v
	 *            Vector
	 * @param factor
	 *            float
	 * @return scaled vector
	 */
	public static Vector div(final Vector v, final float factor) {
		return new Vector(v.x / factor, v.y / factor, v.z / factor);
	}

	/**
	 * Scale a vector by a multiplicative factor
	 * 
	 * @param v
	 *            vector to scale
	 * @param factor
	 * @return scaled vector
	 */
	public static Vector scale(final Vector v, final float factor) {
		return new Vector(v.x * factor, v.y * factor, v.z * factor);
	}

	/**
	 * Dot product of two vectors
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Dot_product">Wikipedia</a>
	 * @param lhs
	 *            Vector
	 * @param rhs
	 *            Vector
	 * @return vector dot-product
	 */
	public static float dot(final Vector lhs, final Vector rhs) {
		return lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;
	}

	/**
	 * Cross product of two vectors
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Cross_product">Wikipedia</a>
	 * @param lhs
	 *            Vector
	 * @param rhs
	 *            Vector
	 * @return vector cross-product
	 */
	public static Vector cross(final Vector lhs, final Vector rhs) {
		return new Vector(lhs.y * rhs.z - lhs.z * rhs.y, lhs.z * rhs.x - lhs.x * rhs.z, lhs.x * rhs.y - lhs.y * rhs.x);
	}

	/**
	 * A linear algebra reflection of a vector off of a surface.
	 * 
	 * @param incoming
	 *            The direction of the incoming Vector
	 * @param normal
	 *            The normal vector of the reflective surface
	 * @return A vector reflection
	 */
	public static Vector reflect(final Vector incoming, final Vector normal) {
		return sub(incoming, scale(normal, 2.0f * dot(incoming, normal)));
	}

	/**
	 * Linear algebra vector refraction. Refracts a vector passing through a
	 * surface.
	 * 
	 * @param incoming
	 *            The direction of incoming vector
	 * @param normal
	 *            Normal vector of the surface
	 * @param eta
	 *            The ratios of the indices of refractions
	 * @return The refracted vector direction
	 * @see <a href="https://en.wikipedia.org/wiki/Snell%27s_law">Wkipedia:
	 *      Snell's Law</a>
	 */
	public static Vector refract(final Vector incoming, final Vector normal, final float eta) {
		final float incedent = dot(incoming, normal);
		final float discriminant = 1.0f - ((eta * eta) * (1.0f - (incedent * incedent)));
		if (discriminant <= 0) {
			// Total internal reflection
			return null;
		}
		return sub(scale(sub(incoming, scale(normal, incedent)), eta), scale(normal, (float) Math.sqrt(discriminant)));
	}

	@Override
	public boolean equals(Object obj) {
		Vector v = (Vector) obj;

		final float error = v.x + v.y + v.z - x - y - z;

		// build in some small tolerance
		return error < 0.00001;
	}

	@Override
	public String toString() {
		return String.format("<%.2f,%.2f,%.2f>", x, y, z);
	}
}
