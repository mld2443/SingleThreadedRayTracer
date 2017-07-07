package Materials;

import Utility.Color;
import Utility.Intersection;
import Utility.Ray;

public class Lambertian extends Material {

	public Lambertian(final Color color) {
		super(color);
	}

	@Override
	public Ray scatter(final Ray incoming, final Intersection intersection, final float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
