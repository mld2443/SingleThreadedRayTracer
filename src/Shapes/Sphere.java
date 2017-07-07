package Shapes;

import Materials.Material;
import Utility.Vector;

/**
 * A sphere is just a Quadric with a special simple normal function.
 * 
 * @author mld2443
 * @see Shapes.Quadric
 */
public class Sphere extends Quadric {

	/**
	 * Constructs a spherical Quadric converting the radius to the required
	 * equation.
	 * 
	 * @param material
	 *            The material the sphere will be made of
	 * @param position
	 *            The offset from the origin
	 * @param radius
	 *            The radius of the sphere
	 */
	public Sphere(final Material material, final Vector position, final float radius) {
		super(material, position, new Quadric.Equation(1, 1, 1, 0, 0, 0, 0, 0, 0, -(radius * radius)));
	}

	/**
	 * Shortcut normal function that computes the normal of a point on a sphere
	 * without computing the generic derivative.
	 * 
	 * @param p
	 *            point on the sphere
	 * @return normal of a sphere
	 */
	@Override
	protected Vector computeNormal(final Vector p) {
		return Vector.sub(p, position).normalize();
	}
}
