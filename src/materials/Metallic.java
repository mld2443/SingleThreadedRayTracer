package materials;

import utilities.Color;
import utilities.Ray;
import utilities.Vector;

/**
 * A metallic material able to emulate metallic objects with different levels of
 * polish. Can have a mirror finish or a somewhat opaque look to it.
 * 
 * @author mld2443
 */
public class Metallic extends Material {
	private final float fuzz;

	/**
	 * Construct a Metallic material. Can have a mirror finish, or a fuzziness
	 * factor.
	 * 
	 * @param color
	 *            The color of this material
	 * @param fuzz
	 *            The fuzziness; 0.0 for mirror finish
	 */
	public Metallic(final Color color, final float fuzz) {
		super(color);
		this.fuzz = fuzz;
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final float sceneIndex) {
		Vector reflected = Vector.reflect(incoming.direction, normal);

		// Apply our fuzziness inside this if block
		if (fuzz > 0.0f)
			reflected = Vector.sum(reflected, Vector.randomInUnitSphere().scale(fuzz));
		
		reflected = reflected.normalize();

		final Ray bounce = new Ray(collision, reflected);

		// Metallic surfaces have the concept of a front and back side; If the
		// ray hits the front, it reflects
		if (Vector.dot(bounce.direction, normal) > 0)
			return bounce;

		// If the ray hits the back side, it is absorbed.
		return null;
	}
}
