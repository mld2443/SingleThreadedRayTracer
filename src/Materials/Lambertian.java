package Materials;

import Utility.Color;
import Utility.Intersection;
import Utility.Ray;

public class Lambertian extends Material {

	public Lambertian(Color color) {
		super(color);
	}

	@Override
	public Ray scatter(Ray incoming, Intersection intersection, float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
