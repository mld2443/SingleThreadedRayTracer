package shapes;

import materials.Material;
import utilities.Range;
import utilities.Ray;
import utilities.Vector;

/**
 * A plane is the only kind of first degree polynomial surface. A single ray can
 * only ever intersect these once.
 * 
 * @author mld2443
 * @see <a href="https://en.wikipedia.org/wiki/Plane_(geometry)">Wikipedia</a>
 */
public class Plane extends Shape {
	private final Vector normal;
	private final float normalDotPosition;

	/**
	 * Constructs a plane.
	 * 
	 * @param material
	 *            The material the plane will be made of (Dielectric not
	 *            recommended)
	 * @param position
	 *            The offset from the origin
	 * @param normal
	 *            The normal vector of the plane
	 */
	public Plane(final Material material, final Vector position, final Vector normal) {
		super(material, position);
		this.normal = normal.normalize();
		// This is a constant in the planar equation, so might as well compute
		// it ahead of time.
		this.normalDotPosition = Vector.dot(this.normal, this.position);
	}

	@Override
	protected Vector computeNormalAt(final Vector point) {
		// This one's pretty simple, though if you'd want to implement a normal
		// map, here's where you would do it
		return normal;
	}

	@Override
	protected Float computeNearestIntersection(final Ray ray, final Range<Float> frustum) {
		final float denominator = Vector.dot(normal, ray.direction);

		// If the ray direction is parallel to our plane, there is no
		// intersection
		if (denominator == 0.0)
			return null;

		// Planar equation
		final float distance = (normalDotPosition - Vector.dot(normal, ray.origin)) / denominator;

		// Return the distance if it's within our bounds
		if (frustum.contains(distance))
			return distance;

		return null;
	}

}
