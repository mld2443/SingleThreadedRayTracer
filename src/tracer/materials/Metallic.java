package tracer.materials;

import java.util.Map;

import tracer.Engine.SceneFormattingException;
import tracer.utils.Color;
import tracer.utils.Ray;
import tracer.utils.Vector;

/**
 * A metallic material able to emulate metallic objects with different levels of
 * polish. Can have a mirror finish or a somewhat opaque look to it.
 * 
 * @author mld2443
 */
public class Metallic extends Material {
	private final double fuzz;

	/**
	 * Construct a Metallic material. Can have a mirror finish, or a fuzziness
	 * factor.
	 * 
	 * @param color
	 *            The color of this material
	 * @param fuzz
	 *            The fuzziness; 0.0 for mirror finish
	 */
	public Metallic(final Color color, final double fuzz) {
		super(color, true);
		this.fuzz = fuzz;
	}

	/**
	 * Constructs a new Metallic material from a list of properties.
	 * 
	 * @param properties
	 *            Map of properties; Expects "color" and "fuzz"
	 * @throws SceneFormattingException
	 */
	public Metallic(final Map<String, String> properties) throws SceneFormattingException {
		this(new Color(properties.get("color")), Float.parseFloat(properties.get("fuzz")));
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final double sceneIndex) {
		Vector reflected = Vector.reflect(incoming.direction, normal);

		// Apply our fuzziness inside this if block
		if (fuzz > 0.0)
			reflected = Vector.sum(reflected, Vector.randomInUnitSphere().scale(fuzz));
		
		reflected = reflected.normalize();

		final Ray bounce = new Ray(collision, reflected);

		// Metallic surfaces have the concept of a front and back side; If the
		// ray hits the front, it reflects
		return bounce;
	}
}
