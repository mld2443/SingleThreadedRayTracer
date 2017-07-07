package Shapes;

import Materials.Material;
import Utility.Intersection;
import Utility.Range;
import Utility.Ray;
import Utility.Vector;

/**
 * A plane is a flat surface with no curvature. A ray can only ever intersect with a plane once.
 * 
 * @author mld2443
 * @see <a href="https://en.wikipedia.org/wiki/Plane_(geometry)">Wikipedia</a>
 */
public class Plane extends Shape {
	public final Vector normal;

	public Plane(final Material material, final Vector position, final Vector normal) {
		super(material, position);
		this.normal = normal;
	}

	@Override
	public Intersection intersectRay(final Ray r, final Range<Float> frustum) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
