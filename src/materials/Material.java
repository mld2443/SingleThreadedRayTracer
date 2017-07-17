package materials;

import utilities.Color;
import utilities.Ray;
import utilities.Vector;

/**
 * Abstract class which defines the common properties of materials.
 * 
 * A material is something that has a color and interacts with light.
 * 
 * @author mld2443
 */
public abstract class Material {
	public final Color color;
	public final Boolean oneSided;
	
	/**
	 * Protected constructor for subclasses to set the color.
	 * 
	 * @param color Color of this material
	 */
	protected Material(final Color color, final Boolean oneSided) {
		this.color = color;
		this.oneSided = oneSided;
	}
	
	/**
	 * Given an incoming Ray, calculates a new Ray that influences the perceived color of this object.
	 * 
	 * @param incoming {@link Ray} which was traced to reach this Material
	 * @param collision Point at which the ray intersected this material surface
	 * @param normal Normal vector of the point of collision
	 * @param sceneIndex Index of refraction for the global scene
	 * @return
	 */
	abstract public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final double sceneIndex);
}
