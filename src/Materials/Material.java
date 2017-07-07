package Materials;

import Utility.Color;
import Utility.Intersection;
import Utility.Ray;

public abstract class Material {
	final Color color;
	
	protected Material(Color color) {
		this.color = color;
	}
	
	abstract public Ray scatter(final Ray incoming, final Intersection intersection, final float sceneIndex);
}
