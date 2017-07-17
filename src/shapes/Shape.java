package shapes;

import materials.Material;
import utilities.Intersection;
import utilities.Range;
import utilities.Ray;
import utilities.Vector;

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
	abstract protected Double computeNearestIntersection(final Ray ray, final Range<Double> frustum);

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
	public Intersection intersectRay(final Ray ray, final Range<Double> frustum) {
		Double distance = computeNearestIntersection(ray, frustum);

		if (distance == null)
			return null;

		final Vector point = ray.project(distance), normal = computeNormalAt(point);
		
		if (material.oneSided && Vector.dot(ray.direction, normal) >= 0.0f)
			return null;

		return new Intersection(distance, point, normal, material);
	}
}
