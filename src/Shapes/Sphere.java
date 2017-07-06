package Shapes;

import Materials.Material;
import Utility.Vector;

/**
 * 
 * @author mld2443
 */
public class Sphere extends Quadric {

	public Sphere(final Material material, final Vector position, final float radius) {
		super(material, position, new Quadric.Equation(1,1,1,0,0,0,0,0,0,-(radius * radius)));
	}
	
	/**
	 * Computes the normal of a point on a sphere
	 * @param p point on the sphere
	 * @return normal of a sphere
	 */
	@Override
	protected Vector computeNormal(final Vector p) {
		return null;
	}
}
