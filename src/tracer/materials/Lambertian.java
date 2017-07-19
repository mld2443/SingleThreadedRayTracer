package tracer.materials;

import java.util.Map;

import tracer.Engine.SceneFormattingException;
import tracer.utils.Color;
import tracer.utils.Ray;
import tracer.utils.Vector;

/**
 * A perfectly diffuse, matte material.
 * 
 * @author mld2443
 * @see <a href="https://en.wikipedia.org/wiki/Lambertian_reflectance">
 *      Wikipedia: Lambertian Reflactance</a>
 */
public class Lambertian extends Material {

	/**
	 * Constructs a new Lambertian material.
	 * 
	 * @param color
	 *            {@link Color} of our new material
	 * @see <a href="https://en.wikipedia.org/wiki/Lambertian_reflectance">
	 *      Wikipedia: Lambertian Reflactance</a>
	 */
	public Lambertian(final Color color) {
		super(color, false);
	}

	/**
	 * Constructs a new Lambertian material from a list of properties.
	 * 
	 * @param properties
	 *            Map of properties; Expects "color"
	 * @throws SceneFormattingException
	 */
	public Lambertian(final Map<String, String> properties) throws SceneFormattingException {
		this(new Color(properties.get("color")));
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final double sceneIndex) {
		Vector target = Vector.sum(collision, normal, Vector.randomInUnitSphere());

		return new Ray(collision, Vector.sub(target, collision));
	}

}
