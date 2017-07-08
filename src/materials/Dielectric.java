package materials;

import utilities.Color;
import utilities.Intersection;
import utilities.Ray;

public class Dielectric extends Material {
	private final float refractionIndex;

	public Dielectric(final Color color, final float refractionIndex) {
		super(color);
		this.refractionIndex = refractionIndex;
	}

	@Override
	public Ray scatter(final Ray incoming, final Intersection intersection, final float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
