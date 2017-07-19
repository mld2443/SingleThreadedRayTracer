package tracer.utils;

import java.util.Random;

import tracer.Engine.SceneFormattingException;

/**
 * A mathematical vector representing either a point or direction in
 * 3-Dimensional space.
 * 
 * @author mld2443
 */
public final class Vector {
	// TODO: determine speed difference if using doubles
	public final double x, y, z;
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
	public Vector(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructs a Vector from a string description.
	 * 
	 * @param desc
	 *            Descriptor of the format "(x, y, z)"
	 * @throws Engine.SceneFormattingException
	 */
	public Vector(final String desc) throws SceneFormattingException {
		try {
			if (desc.startsWith("(") && desc.endsWith(")")) {
				String[] values = desc.substring(1, desc.length() - 1).split(",");

				if (values.length != 3)
					throw new SceneFormattingException("Unknown Vector format: " + desc);

				x = Double.parseDouble(values[0]);
				y = Double.parseDouble(values[1]);
				z = Double.parseDouble(values[2]);
			} else
				throw new SceneFormattingException("Unknown Vector format: " + desc);
		} catch (NumberFormatException e) {
			throw new SceneFormattingException("Unknown Vector format: " + desc);
		}
	}

	/**
	 * @return the length of the vector
	 */
	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * @return a unit vector with the same direction
	 */
	public Vector normalize() {
		return divide(magnitude());
	}

	/**
	 * @return a randomly generated vector with a length of 1.0
	 */
	public static Vector randomInUnitSphere() {
		return new Vector(gen.nextGaussian(), gen.nextGaussian(), gen.nextGaussian()).normalize();
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
	 * Vector summation. Takes any number of vectors.
	 * 
	 * @param operands
	 *            Variable number of operands
	 * @return Vector summation
	 */
	public static Vector sum(final Vector... operands) {
		double x = 0.0, y = 0.0, z = 0.0;

		for (Vector v : operands) {
			x += v.x;
			y += v.y;
			z += v.z;
		}

		return new Vector(x, y, z);
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
	 *            double
	 * @return scaled vector
	 */
	public Vector divide(final double factor) {
		return new Vector(x / factor, y / factor, z / factor);
	}

	/**
	 * Scale a vector by a multiplicative factor
	 * 
	 * @param v
	 *            vector to scale
	 * @param factor
	 *            double
	 * @return scaled vector
	 */
	public Vector scale(final double factor) {
		return new Vector(x * factor, y * factor, z * factor);
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
	public static double dot(final Vector lhs, final Vector rhs) {
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
		return sub(incoming, normal.scale(2.0 * dot(incoming, normal)));
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
	public static Vector refract(final Vector incoming, final Vector normal, final double eta) {
		final double cosI = -dot(incoming, normal);
		final double sinT2 = eta * eta * (1.0 - cosI * cosI);

		if (sinT2 > 1.0f) {
			// Total internal reflection
			return null;
		}

		final double cosT = Math.sqrt(1.0 - sinT2);

		return Vector.sum(incoming.scale(eta), normal.scale(eta * cosI - cosT));
	}

	@Override
	public boolean equals(Object obj) {
		Vector v = (Vector) obj;

		final double error = v.x + v.y + v.z - x - y - z;

		// build in some small tolerance
		return error < 0.00001;
	}

	@Override
	public String toString() {
		return String.format("<%.2f,%.2f,%.2f>", x, y, z);
	}
}
