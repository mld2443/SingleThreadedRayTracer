package tracer.materials;

import java.util.Map;
import java.util.Random;

import tracer.Engine.SceneFormattingException;
import tracer.utils.Color;
import tracer.utils.Ray;
import tracer.utils.Vector;

/**
 * A dielectric is a transparent, glassy material.
 * 
 * @author mld2443
 */
public class Dielectric extends Material {
	private final double refractionIndex;

	/**
	 * A static Random object with which to generate random floats [0,1).
	 */
	private static final Random rand = new Random();

	/**
	 * Constructor for a new dielectric material. For reference, the refraction
	 * index of a vacuum (and approximately air) is 1.0; water is about 1.33;
	 * glass is closer to 1.52 to 1.62; diamond is about 2.42.
	 * 
	 * @param color
	 *            Color of the material; Black will make it completely opaque
	 * @param refractionIndex
	 *            The index of refraction for this material
	 * @see <a href="https://en.wikipedia.org/wiki/Refractive_index">Wikipedia:
	 *      Refractive Index</a>
	 */
	public Dielectric(final Color color, final double refractionIndex) {
		// Since refracted materials tend to appear darker, we lighten them up
		// so the color appears correct
		super(color.applyTransform(channel -> Math.sqrt(channel)), false);
		this.refractionIndex = refractionIndex;
	}

	/**
	 * Constructs a new Dielectric material from a list of properties.
	 * 
	 * @param properties
	 *            Map of properties; Expects "color" and "index"
	 * @throws SceneFormattingException
	 */
	public Dielectric(final Map<String, String> properties) throws SceneFormattingException {
		this(new Color(properties.get("color")), Float.parseFloat(properties.get("index")));
	}

	/**
	 * The Schlick approximation of the Fresnel equation.
	 * 
	 * @param cosine
	 *            Cosine of the angle between incidents
	 * @param sceneIndex
	 *            Index of refraction outside of our Dielectric
	 * @return Probability of refraction
	 * @see <a href="https://en.wikipedia.org/wiki/Schlick%27s_approximation">
	 *      Wikipedia: Schlick's Approximation</a>
	 */
	private double schlickApproximation(final double cosX, final double sceneIndex) {
		double r0 = (sceneIndex - refractionIndex) / (sceneIndex + refractionIndex);
		r0 *= r0;
		final double x = 1.0 - cosX;
		return r0 + (1.0 - r0) * x * x * x * x * x;
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final double sceneIndex) {
		final double entering = Vector.dot(incoming.direction, normal);
		double cosX;
		Vector refracted;

		if (entering > 0) {
			// The ray is outside and potentially entering the object
			cosX = entering;
			refracted = Vector.refract(incoming.direction, normal.invert(), refractionIndex / sceneIndex);
		} else {
			// the ray is inside and potentially exiting the object
			cosX = -entering;
			refracted = Vector.refract(incoming.direction, normal, sceneIndex / refractionIndex);
		}

		// Reflect with probability given by the Schlick approximation, or if
		// there's Total Internal Reflection
		if (refracted == null || rand.nextDouble() < schlickApproximation(cosX, sceneIndex)) {
			return new Ray(collision, Vector.reflect(incoming.direction, normal));
		}

		// Refract
		return new Ray(collision, refracted);
	}

}
