package materials;

import utilities.Color;
import utilities.Ray;
import utilities.Vector;

public class Lambertian extends Material {

	public Lambertian(final Color color) {
		super(color);
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
