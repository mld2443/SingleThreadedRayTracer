package Shapes;

import Materials.Material;
import Utility.Intersection;
import Utility.Ray;
import Utility.Vector;

public abstract class Shape {
	Material material;
	Vector position;
	
	protected Shape(final Material material, final Vector position) {
		this.material = material;
		this.position = position;
	}
	
	abstract public Intersection intersectRay(final Ray r);
}
