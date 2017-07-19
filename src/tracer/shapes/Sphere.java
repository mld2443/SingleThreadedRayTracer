package tracer.shapes;

import java.util.Map;

import tracer.Engine.SceneFormattingException;
import tracer.materials.Material;
import tracer.utils.Vector;

/**
 * A sphere is just a Quadric with a special simple normal function.
 * 
 * @author mld2443
 * @see tracer.shapes.Quadric
 */
public class Sphere extends Quadric {

	/**
	 * Constructs a spherical Quadric converting the radius to the required
	 * equation.
	 * 
	 * @param material
	 *            The material the sphere will be made of
	 * @param position
	 *            The offset from the origin
	 * @param radius
	 *            The radius of the sphere
	 */
	public Sphere(final Material material, final Vector position, final double radius) {
		super(material, position, new Quadric.Equation(1, 1, 1, 0, 0, 0, 0, 0, 0, -(radius * radius)));
	}

	/**
	 * Constructs a new spherical {@link Quadric} from a list of properties,
	 * converting the radius to the required equation.
	 * 
	 * @param material
	 *            The material the sphere will be made of
	 * @param properties
	 *            Map of properties; Expects "position" and "radius"
	 * @throws SceneFormattingException
	 */
	public Sphere(Material material, Map<String, String> properties) throws SceneFormattingException {
		this(material, new Vector(properties.get("position")), Double.parseDouble(properties.get("radius")));
	}

	/**
	 * Shortcut normal function that computes the normal of a point on a sphere
	 * without computing the generic derivative.
	 */
	@Override
	protected Vector computeNormalAt(final Vector point) {
		return Vector.sub(point, position).normalize();
	}
}
