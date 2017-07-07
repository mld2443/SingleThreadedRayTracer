import java.io.File;
import java.util.ArrayList;

import Shapes.Shape;

public class Scene {
	ArrayList<Shape> things;
	public final float refractionIndex;
	
	public Scene() {
		refractionIndex = 1.0f;
		// TODO: generate a random scene here
	}
	
	public Scene(File descriptor) {
		refractionIndex = 1.0f;
		// TODO: read a file here
	}
}
