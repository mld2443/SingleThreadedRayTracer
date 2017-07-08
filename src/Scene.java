import java.io.File;
import java.util.ArrayList;

import Materials.Lambertian;
import Materials.Material;
import Shapes.Plane;
import Shapes.Shape;
import Shapes.Sphere;
import Utility.Color;
import Utility.Vector;

public class Scene {
	ArrayList<Shape> things;
	public final float refractionIndex;
	
	public Scene() {
		refractionIndex = 1.0f;
		this.things = new ArrayList<Shape>();
		
		// TODO: generate a random scene here
		Material matteWhite = new Lambertian(Color.white());
		Material matteGreen = new Lambertian(Color.green());
		
		things.add(new Plane(matteWhite, new Vector(), new Vector(0,0,1)));
		things.add(new Sphere(matteGreen, new Vector(5,0,3), 3));
	}
	
	public Scene(File descriptor) {
		refractionIndex = 1.0f;
		this.things = new ArrayList<Shape>();
		
		// TODO: read a file here
	}
}
