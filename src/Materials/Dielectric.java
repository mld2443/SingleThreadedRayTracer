package Materials;

import Utility.Color;
import Utility.Intersection;
import Utility.Ray;

public class Dielectric extends Material {
	float refractionIndex;

	public Dielectric(Color color, final float refractionIndex) {
		super(color);
		this.refractionIndex = refractionIndex;
	}

	@Override
	public Ray scatter(Ray incoming, Intersection intersection, float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
