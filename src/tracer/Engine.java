package tracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import materials.Dielectric;
import materials.Lambertian;
import materials.Material;
import materials.Metallic;
import shapes.Plane;
import shapes.Quadric;
import shapes.Sphere;
import utilities.FileParser;
import utilities.FileParser.Entry;
import utilities.Vector;

/**
 * An Engine sets up our camera and scene.
 * 
 * @author mld2443
 */
public class Engine {
	@SuppressWarnings("serial")
	public static class SceneFormattingException extends RuntimeException {
		public SceneFormattingException(String message) {
			super(message);
		}
	}

	Scene scene;
	Camera camera;

	public Engine(final String filename) throws IOException, SceneFormattingException {
		List<Entry> descriptors = FileParser.parse(filename);
		
		Map<String, Material> materials = new HashMap<>();

		for (Entry entry : descriptors) {
			try {
				switch (entry.type) {
				case "scene":
					final float index = Float.parseFloat(entry.properties.get("index"));
					scene = new Scene(index);
					break;
					
				case "camera":
					final Vector pos = new Vector(entry.properties.get("position"));
					final Vector dir = new Vector(entry.properties.get("direction"));
					final float fov = Float.parseFloat(entry.properties.get("fov"));
					camera = new Camera(pos, dir, 800, 400, 100, 10, fov);
					break;
					
					
				case "lambertian":
					materials.put(entry.name, new Lambertian(entry.properties));
					break;
					
				case "metallic":
					materials.put(entry.name, new Metallic(entry.properties));
					break;
					
				case "dielectric":
					materials.put(entry.name, new Dielectric(entry.properties));
					break;
					
					
				case "plane":
				case "quadric":
				case "sphere":
					final String materialName = entry.properties.get("material");
					final Material material = materials.get(materialName);
					if (material == null)
						throw new SceneFormattingException("Undefuned material: " + materialName);
					
					if (entry.type.equals("plane"))
						scene.addShape(new Plane(material, entry.properties));
					else if (entry.type.equals("quadric"))
						scene.addShape(new Quadric(material, entry.properties));
					else
						scene.addShape(new Sphere(material, entry.properties));
					break;
					
				default:
					throw new SceneFormattingException("Unknown type: " + entry.type);
				}
			} catch (NumberFormatException e) {
				throw new SceneFormattingException(
						"Improper number format for entry \"" + entry.type + " " + entry.name + "\"");
			}
		}
	}
	
	public void saveCaptureTo(final String filename) throws IOException {
		
		// retrieve image
		BufferedImage image = camera.captureScene(scene);

		//t.eventStart("Writing image to file");

		File output = new File(filename);
		ImageIO.write(image, "png", output);

		//t.eventStop("Writing image to file");
		
		//bi = t.gridHeatmap();
		
		//t.eventStart("Write the heatmap");
		
		//outputfile = new File("heatmap.png");
		//ImageIO.write(bi, "png", outputfile);
		
		//t.eventStop("Write the heatmap");
	}

}
