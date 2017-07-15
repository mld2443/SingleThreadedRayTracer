package shapes;

import java.util.Map;

import materials.Material;
import tracer.Engine.SceneFormattingException;
import utilities.Range;
import utilities.Ray;
import utilities.Vector;

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
		final float A, B, C, D, E, F, G, H, I, J;
		final Vector ABC, DEF, GHI;

		public Equation(final float A, final float B, final float C, final float D, final float E, final float F,
				final float G, final float H, final float I, final float J) {
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

					this.A = Float.parseFloat(values[0]);
					this.B = Float.parseFloat(values[1]);
					this.C = Float.parseFloat(values[2]);
					this.D = Float.parseFloat(values[3]);
					this.E = Float.parseFloat(values[4]);
					this.F = Float.parseFloat(values[5]);
					this.G = Float.parseFloat(values[6]);
					this.H = Float.parseFloat(values[7]);
					this.I = Float.parseFloat(values[8]);
					this.J = Float.parseFloat(values[9]);
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
		final Vector relative = Vector.sub(point, position);

		// This is the literal derivative of the Quadric at point p
		final float dx = (2 * equation.A * relative.x + equation.E * relative.z + equation.F * relative.y + equation.G);
		final float dy = (2 * equation.B * relative.y + equation.D * relative.z + equation.F * relative.x + equation.H);
		final float dz = (2 * equation.C * relative.z + equation.D * relative.y + equation.E * relative.x + equation.I);

		return new Vector(dx, dy, dz).normalize();
	}

	@Override
	protected Float computeNearestIntersection(final Ray ray, final Range<Float> frustum) {
		// Calculate the positions of the camera and the ray relative to the
		// quadric
		Vector rCam = Vector.sub(ray.origin, position);
		Vector rRay = ray.direction;

		// Pre-calculate these values for our quadratic equation; doing it this
		// way would make more sense if our vector operations were SIMD
		Vector V1 = Vector.inlineMultiply(rRay, rRay);
		Vector V2 = new Vector(rRay.x * rRay.y, rRay.y * rRay.z, rRay.z * rRay.x).scale(2);
		Vector V3 = Vector.inlineMultiply(rCam, rRay);
		Vector V4 = new Vector(rRay.x * rCam.y + rCam.x * rRay.y, rCam.y * rRay.z + rRay.y * rCam.z,
				rCam.x * rRay.z + rRay.x * rCam.z);
		Vector V5 = rRay;
		Vector V6 = Vector.inlineMultiply(rCam, rCam);
		Vector V7 = new Vector(rCam.x * rCam.y, rCam.y * rCam.z, rCam.z * rCam.x).scale(2);
		Vector V8 = rCam.scale(2);

		// Calculate the quadratic coefficients
		float A = Vector.dot(equation.ABC, V1) + Vector.dot(equation.DEF, V2);
		float B = Vector.dot(equation.ABC, V3) + Vector.dot(equation.DEF, V4) + Vector.dot(equation.GHI, V5);
		float C = Vector.dot(equation.ABC, V6) + Vector.dot(equation.DEF, V7) + Vector.dot(equation.GHI, V8)
				+ equation.J;

		// Calculate the squared value for our quadratic formula
		float square = B * B - A * C;

		// No collision if the root is imaginary
		if (square < 0)
			return null;

		// Take its square root if it's real
		float root = (float) Math.sqrt(square);

		// Calculate both intersections
		float D1 = (-B - root) / A;
		float D2 = (-B + root) / A;

		// Return closest intersection thats in the frustum
		if (frustum.contains(D1))
			return D1;
		else if (frustum.contains(D2))
			return D2;
		return null;
	}

}
