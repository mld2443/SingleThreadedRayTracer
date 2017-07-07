package Shapes;

import Materials.Material;
import Utility.Intersection;
import Utility.Range;
import Utility.Ray;
import Utility.Vector;

/**
 * A quadric is a shape whose surface can be described by the function: <br>
 * {@code Ax² + By² + Cz² + 2Dyz + 2Exz + 2Fxy + 2Gx + 2Hy + 2Iz + J = 0}
 * 
 * @author mld2443
 * @see <a href="https://en.wikipedia.org/wiki/Quadric">Wikipedia</a>
 */
public class Quadric extends Shape {
	public static class Equation {
		float A, B, C, D, E, F, G, H, I, J;

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
		}
	}

	Equation equation;

	public Quadric(final Material material, final Vector position, final Equation equation) {
		super(material, position);
		this.equation = equation;
	}

	protected Vector computeNormal(final Vector p) {
		// TODO Implement this
		return null;
	}

	@Override
	public Intersection intersectRay(final Ray r, final Range frustum) {
		// TODO Auto-generated method stub
		return null;
	}
}
