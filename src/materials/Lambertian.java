package materials;

import utilities.Color;
import utilities.Ray;
import utilities.Vector;

/**
 * A perfectly diffuse, matte material.
 * 
 * @author mld2443
 * @see <a href="https://en.wikipedia.org/wiki/Lambertian_reflectance">Wikipedia: Lambertian Reflactance</a>
 */
public class Lambertian extends Material {

	/**
	 * Constructs a new Lambertian material.
	 * 
	 * @param color {@link Color} of our new material
	 * @see <a href="https://en.wikipedia.org/wiki/Lambertian_reflectance">Wikipedia: Lambertian Reflactance</a> 
	 */
	public Lambertian(final Color color) {
		super(color);
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final float sceneIndex) {
		Vector target = Vector.sum(collision, normal, Vector.randomInUnitSphere());
        
        return new Ray(collision, Vector.sub(target, collision));
	}

}
