package materials;

import utilities.Color;
import utilities.Ray;
import utilities.Vector;

public class Metallic extends Material {
	private final float fuzz;

	public Metallic(final Color color, final float fuzz) {
		super(color);
		this.fuzz = fuzz;
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
