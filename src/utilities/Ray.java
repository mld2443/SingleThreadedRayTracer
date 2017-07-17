package utilities;

/**
 * A class defining a geometric ray, with an origin and direction. 
 * 
 * @author mld2443
 */
public class Ray {
	public final Vector origin, direction;
	
	/**
	 * Construct a new Ray.
	 * @param origin
	 * @param direction
	 */
	public Ray(final Vector origin, final Vector direction) {
		this.origin = origin;
		// We normalize the direction so the projection works nicely.
		this.direction = direction.normalize();
	}
	
	/**
	 * Calculate a point that is 'distance' away from the Ray's origin along the Ray.
	 * @param distance
	 * @return
	 */
	public Vector project(final double distance) {
		return Vector.sum(origin, direction.scale(distance));
	}
	
	@Override
	public String toString() {
		return String.format("o:%s d:%s", origin, direction);
	}
}
