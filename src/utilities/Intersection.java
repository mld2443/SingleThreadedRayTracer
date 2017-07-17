package utilities;

import materials.Material;

public class Intersection {
	public final double distance;
	public final Vector point;
	public final Vector normal;
	public final Material material;

	public Intersection(final double distance, final Vector point, final Vector normal, final Material material) {
		this.distance = distance;
		this.point = point;
		this.normal = normal;
		this.material = material;
	}
}
