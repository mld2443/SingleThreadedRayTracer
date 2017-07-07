package Materials;

import Utility.Color;
import Utility.Intersection;
import Utility.Ray;

public class Metallic extends Material {
	private final float fuzz;

	public Metallic(final Color color, final float fuzz) {
		super(color);
		this.fuzz = fuzz;
	}

	@Override
	public Ray scatter(final Ray incoming, final Intersection intersection, final float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
