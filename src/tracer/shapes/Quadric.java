package tracer.shapes;

import java.util.Map;

import tracer.Engine.SceneFormattingException;
import tracer.materials.Material;
import tracer.utils.Range;
import tracer.utils.Ray;
import tracer.utils.Vector;

/**
 * A Quadric is a generalized second degree polynomial surface. Just like with a
 * second degree polynomial function and a straight line, a single ray cannot
 * intersect a Quadric more than twice.
 * 
 * @author mld2443
 * @see <a href="https://en.wikipedia.org/wiki/Quadric">Wikipedia</a>
 */
public class Quadric extends Shape {

	/**
	 * Simple container for the coefficients of the 3D quadratic equation:<br>
	 * {@code Ax² + By² + Cz² + 2Dyz + 2Exz + 2Fxy + 2Gx + 2Hy + 2Iz + J = 0}
	 * 
	 * @author mld2443
	 */
	public static class Equation {
		final double A, B, C, D, E, F, G, H, I, J;
		final Vector ABC, DEF, GHI;

		public Equation(final double A, final double B, final double C, final double D, final double E, final double F,
				final double G, final double H, final double I, final double J) {
			this.A = A;
			this.B = B;
			this.C = C;
			this.D = D;
			this.E = E;
			this.F = F;
			this.G = G;
			this.H = H;
			this.I = I;
			this.J = J;
			ABC = new Vector(A, B, C);
			DEF = new Vector(D, E, F);
			GHI = new Vector(G, H, I);
		}

		/**
		 * Constructs a Vector from a string description.
		 * 
		 * @param desc
		 *            Descriptor of the format "(A, B, C, D, E, F, G, H, I, J)"
		 * @throws Engine.SceneFormattingException
		 */
		public Equation(final String desc) throws SceneFormattingException {
			try {
				if (desc.startsWith("(") && desc.endsWith("")) {
					String[] values = desc.substring(1, desc.length() - 1).split(",");

					if (values.length != 10)
						throw new SceneFormattingException("Unknown Equation format: " + desc);

					this.A = Double.parseDouble(values[0]);
					this.B = Double.parseDouble(values[1]);
					this.C = Double.parseDouble(values[2]);
					this.D = Double.parseDouble(values[3]);
					this.E = Double.parseDouble(values[4]);
					this.F = Double.parseDouble(values[5]);
					this.G = Double.parseDouble(values[6]);
					this.H = Double.parseDouble(values[7]);
					this.I = Double.parseDouble(values[8]);
					this.J = Double.parseDouble(values[9]);
					ABC = new Vector(A, B, C);
					DEF = new Vector(D, E, F);
					GHI = new Vector(G, H, I);
				} else
					throw new SceneFormattingException("Unknown Equation format: " + desc);
			} catch (NumberFormatException e) {
				throw new SceneFormattingException("Unknown Equation format: " + desc);
			}
		}
	}

	private final Equation equation;

	/**
	 * Constructs a Quadric with the given equation.
	 * 
	 * @param material
	 *            The material the Quadric will be made of
	 * @param position
	 *            The offset from the origin
	 * @param Equation
	 *            The coefficients of the quadratic equation
	 */
	public Quadric(final Material material, final Vector position, final Equation equation) {
		super(material, position);
		this.equation = equation;
	}

	/**
	 * Constructs a new Quadric from a list of properties.
	 * 
	 * @param material
	 *            The material the Quadric will be made of
	 * @param properties
	 *            Map of properties; Expects "position" and "equation"
	 * @throws SceneFormattingException
	 */
	public Quadric(Material material, Map<String, String> properties) throws SceneFormattingException {
		this(material, new Vector(properties.get("position")), new Equation(properties.get("equation")));
	}

	/**
	 * The Quadric normal function computes the generic derivative of the
	 * quadratic equation for the point.
	 */
	@Override
	protected Vector computeNormalAt(final Vector point) {
		// Relative direction of the point to the Quadric
		final Vector r = Vector.sub(point, position);

		// This is the literal derivative of the Quadric at point p
		final double dx = (2 * equation.A * r.x + equation.E * r.z + equation.F * r.y + equation.G);
		final double dy = (2 * equation.B * r.y + equation.D * r.z + equation.F * r.x + equation.H);
		final double dz = (2 * equation.C * r.z + equation.D * r.y + equation.E * r.x + equation.I);

		return new Vector(dx, dy, dz).normalize();
	}

	@Override
	protected Double computeNearestIntersection(final Ray ray, final Range<Double> frustum) {
		// Calculate the positions of the camera and the ray relative to the
		// quadric
		final Vector rCam = Vector.sub(ray.origin, position);
		final Vector rRay = ray.direction;

		// Pre-calculate these values for our quadratic equation; doing it this
		// way would make more sense if our vector operations were SIMD
		final Vector V1 = Vector.inlineMultiply(rRay, rRay);
		final Vector V2 = new Vector(rRay.x * rRay.y, rRay.y * rRay.z, rRay.z * rRay.x).scale(2);
		final Vector V3 = Vector.inlineMultiply(rCam, rRay);
		final Vector V4 = new Vector(rRay.x * rCam.y + rCam.x * rRay.y, rCam.y * rRay.z + rRay.y * rCam.z,
				rCam.x * rRay.z + rRay.x * rCam.z);
		final Vector V5 = rRay;
		final Vector V6 = Vector.inlineMultiply(rCam, rCam);
		final Vector V7 = new Vector(rCam.x * rCam.y, rCam.y * rCam.z, rCam.z * rCam.x).scale(2);
		final Vector V8 = rCam.scale(2);

		// Calculate the quadratic coefficients
		final double A = Vector.dot(equation.ABC, V1) + Vector.dot(equation.DEF, V2);
		final double B = Vector.dot(equation.ABC, V3) + Vector.dot(equation.DEF, V4) + Vector.dot(equation.GHI, V5);
		final double C = Vector.dot(equation.ABC, V6) + Vector.dot(equation.DEF, V7) + Vector.dot(equation.GHI, V8)
				+ equation.J;

		// Calculate the squared value for our quadratic formula
		final double square = B * B - A * C;

		// No collision if the root is imaginary
		if (square < 0)
			return null;

		// Take its square root if it's real
		final double root = Math.sqrt(square);

		// Calculate both intersections
		final double D1 = (-B - root) / A;
		final double D2 = (-B + root) / A;

		// Return closest intersection thats in the frustum
		if (frustum.contains(D1))
			return D1;
		else if (frustum.contains(D2))
			return D2;
		return null;
	}

}
