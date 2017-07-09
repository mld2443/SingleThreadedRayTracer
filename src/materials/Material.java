package materials;

import utilities.Color;
import utilities.Ray;
import utilities.Vector;

public abstract class Material {
	public final Color color;
	
	protected Material(Color color) {
		this.color = color;
	}
	
	abstract public Ray scatter(final Ray incoming, final Vector collision, final Vector normal, final float sceneIndex);
}
