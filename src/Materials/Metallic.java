package Materials;

import Utility.Color;
import Utility.Intersection;
import Utility.Ray;

public class Metallic extends Material {
	float fuzz;

	public Metallic(Color color, final float fuzz) {
		super(color);
		this.fuzz = fuzz;
	}

	@Override
	public Ray scatter(Ray incoming, Intersection intersection, float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
