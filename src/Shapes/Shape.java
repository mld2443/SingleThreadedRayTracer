package Shapes;

import Materials.Material;
import Utility.Intersection;
import Utility.Range;
import Utility.Ray;
import Utility.Vector;

/**
 * A shape is an object that resides in 3D space, and can interact with our rays.
 * 
 * @author mld2443
 */
public abstract class Shape {
	public final Material material;
	public final Vector position;

	/**
	 * Constructor for child classes.
	 * @param material
	 * @param position
	 */
	protected Shape(final Material material, final Vector position) {
		this.material = material;
		this.position = position;
	}

	/**
	 * Computes the normal Vector of a point on the Shape.
	 * 
	 * @param point
	 *            Point of the surface to find a normal for
	 * @return A unit vector normal to the surface
	 */
	abstract protected Vector computeNormalAt(final Vector point);

	/**
	 * Computes the distance of the nearest intersection with the Shape.
	 * 
	 * @param ray
	 *            ray to trace
	 * @param frustum
	 *            range within to register a collision
	 * @return the unit distance closest intersection if there is one within
	 *         range, otherwise null
	 */
	abstract protected Float computeNearestIntersection(final Ray ray, final Range<Float> frustum);

	/**
	 * Check if our mathematically defined ray intersects our mathematically
	 * defined shape inside the frustum.
	 * 
	 * @param ray
	 *            ray to trace
	 * @param frustum
	 *            range within to register a collision
	 * @return the closest intersection if there is one within range, otherwise
	 *         null
	 */
	public Intersection intersectRay(final Ray ray, final Range<Float> frustum) {
		Float distance = computeNearestIntersection(ray, frustum);

		if (distance == null)
			return null;

		Vector point = ray.project(distance);

		return new Intersection(distance, point, computeNormalAt(point));
	}
}
