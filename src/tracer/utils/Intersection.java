package tracer.utils;

import tracer.materials.Material;

/**
 * An intersection is for us only an intermediate data type, containing all
 * relevant information pertaining to a ray-surface intersection.
 * 
 * @author mld2443
 */
public class Intersection {
	public final double distance;
	public final Vector point;
	public final Vector normal;
	public final Material material;

	/**
	 * The default constructor.
	 * 
	 * @param distance
	 *            Positive number representing the distance from the origin of
	 *            the ray this intersection occurred
	 * @param point
	 *            The point at which our intersection took place
	 * @param normal
	 *            The normal of the surface which we intersected
	 * @param material
	 *            The {@link Material} that surface was made of
	 */
	public Intersection(final double distance, final Vector point, final Vector normal, final Material material) {
		this.distance = distance;
		this.point = point;
		this.normal = normal;
		this.material = material;
	}
}
