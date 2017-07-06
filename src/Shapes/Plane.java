package Shapes;

import Materials.Material;
import Utility.Intersection;
import Utility.Ray;
import Utility.Vector;

public class Plane extends Shape {
	Vector normal;

	public Plane(final Material material, final Vector position, final Vector normal) {
		super(material, position);
		this.normal = normal;
	}

	@Override
	public Intersection intersectRay(Ray r) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
