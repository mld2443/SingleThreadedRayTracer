package materials;

import utilities.Color;
import utilities.Ray;
import utilities.Vector;

public class Dielectric extends Material {
	private final float refractionIndex;

	public Dielectric(final Color color, final float refractionIndex) {
		super(color);
		this.refractionIndex = refractionIndex;
	}

	@Override
	public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final float sceneIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
