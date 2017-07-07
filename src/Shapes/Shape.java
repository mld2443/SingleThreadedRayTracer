package Shapes;

import Materials.Material;
import Utility.Intersection;
import Utility.Range;
import Utility.Ray;
import Utility.Vector;

public abstract class Shape {
	public final Material material;
	public final Vector position;

	protected Shape(final Material material, final Vector position) {
		this.material = material;
		this.position = position;
	}

	/**
	 * Check if our mathematically defined ray intersects our mathematically
	 * defined shape inside the frustum
	 * 
	 * @param r
	 *            ray to trace
	 * @param frustum
	 *            range within to register a collision
	 * @return the closest intersection if there is one within range, otherwise
	 *         null
	 */
	abstract public Intersection intersectRay(final Ray r, final Range<Float> frustum);
}
