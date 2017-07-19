package tracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import tracer.materials.Dielectric;
import tracer.materials.Lambertian;
import tracer.materials.Material;
import tracer.materials.Metallic;
import tracer.shapes.Plane;
import tracer.shapes.Quadric;
import tracer.shapes.Sphere;
import tracer.utils.FileParser;
import tracer.utils.GridTimerDelegate;
import tracer.utils.Vector;
import tracer.utils.FileParser.Entry;

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

	private Scene scene = null;
	private Camera camera = null;
	private GridTimerDelegate timer;

	public Engine(final String filename, final int width, final int height, final int sampling, final int depth,
			GridTimerDelegate timer) throws IOException, SceneFormattingException {
		this.timer = timer;

		this.timer.eventStart("Parse file \"" + filename + "\"");
		final List<Entry> descriptors = FileParser.parse(filename);
		this.timer.eventStop("Parse file \"" + filename + "\"");

		this.timer.eventStart("Allocate Scene and Camera");
		allocateFromEntries(descriptors, width, height, sampling, depth);
		this.timer.eventStop("Allocate Scene and Camera");

		if (this.scene == null)
			throw new SceneFormattingException("File missing Scene descriptor");
		if (this.camera == null)
			throw new SceneFormattingException("File missing Camera descriptor");
	}

	private void allocateFromEntries(final List<Entry> descriptors, final int width, final int height,
			final int sampling, final int depth) throws SceneFormattingException {
		Map<String, Material> materials = new HashMap<>();

		for (final Entry entry : descriptors) {
			try {
				switch (entry.type) {
				case "scene":
					final double index = Double.parseDouble(entry.properties.get("index"));
					this.scene = new Scene(index);
					break;

				case "camera":
					final Vector pos = new Vector(entry.properties.get("position"));
					final Vector dir = new Vector(entry.properties.get("direction"));
					final double fov = Double.parseDouble(entry.properties.get("fov"));
					this.camera = new Camera(pos, dir, width, height, sampling, depth, fov);
					this.camera.setTimer(this.timer);
					Vector up;
					if ((up = new Vector(entry.properties.get("position"))) != null) {
						camera.aimCamera(fov, dir, up);
					}
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
						this.scene.addShape(new Plane(material, entry.properties));
					else if (entry.type.equals("quadric"))
						this.scene.addShape(new Quadric(material, entry.properties));
					else
						this.scene.addShape(new Sphere(material, entry.properties));
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
		// Get our capture
		final BufferedImage image = camera.captureScene(scene);

		if (timer != null)
			timer.eventStart("Writing image to file");

		File output = new File(filename);
		ImageIO.write(image, "png", output);

		if (timer != null)
			timer.eventStop("Writing image to file");
	}

	public void savePreviewTo(final String filename) throws IOException {
		// Get our preview
		final BufferedImage preview = camera.preview(scene);

		if (timer != null)
			timer.eventStart("Writing preview to file");

		File output = new File(filename);
		ImageIO.write(preview, "png", output);

		if (timer != null)
			timer.eventStop("Writing preview to file");
	}

}
