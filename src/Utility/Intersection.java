package Utility;

public class Intersection {
	final float distance;
	final Vector point;
	final Vector normal;

	public Intersection(final float distance, final Vector point, final Vector normal) {
		this.distance = distance;
		this.point = point;
		this.normal = normal;
	}
}
