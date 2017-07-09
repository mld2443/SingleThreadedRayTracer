import java.io.File;
import java.util.ArrayList;

import materials.Lambertian;
import materials.Material;
import shapes.Plane;
import shapes.Shape;
import shapes.Sphere;
import utilities.Color;
import utilities.Intersection;
import utilities.Range;
import utilities.Ray;
import utilities.Vector;

/**
 * The Scene class is a container for all the objects and properties necessary
 * to describe a scene. A {@link Camera} can {@link Camera#captureScene(Scene)
 * capture} a Scene.
 * 
 * @author mld2443
 */
public class Scene {
	/**
	 * The index of refraction of the space outside of any objects.
	 */
	public final float refractionIndex;

	/**
	 * The color of the sky. This is used in {@link Scene#skyBox(Vector)}.
	 */
	public Color sky = new Color(0.5f, 0.7f, 1.0f);

	/**
	 * This is the container for our scene's shapes.
	 */
	private ArrayList<Shape> things;

	/**
	 * Random Scene generator. Using a random seed, this constructor generates a
	 * floor and a bunch of spheres scattered about near the origin.
	 * 
	 * @param seed
	 *            An integer seed
	 */
	public Scene() {
		refractionIndex = 1.0f;
		this.things = new ArrayList<Shape>();

		// TODO: generate a random scene here
		Material matteWhite = new Lambertian(Color.white());
		Material matteGreen = new Lambertian(Color.green());

		things.add(new Plane(matteWhite, new Vector(), new Vector(0, 0, 1)));
		things.add(new Sphere(matteGreen, new Vector(5, 0, 3), 3));
	}

	/**
	 * Scene constructor that loads a custom scene described in a {@link File}.
	 * 
	 * @param descriptor
	 *            A file containing a scene description
	 */
	public Scene(File descriptor) {
		refractionIndex = 1.0f;
		this.things = new ArrayList<Shape>();

		// TODO: read a file here
	}

	/**
	 * Generate a color based on where you are looking. This function describes
	 * a gradient that is white below the horizon and fades to {@link Scene#sky
	 * sky color} the more upward you look.
	 * 
	 * @param direction
	 *            The direction in which to sample our gradient
	 * @return A color from the gradient
	 */
	private Color skyBox(final Vector direction) {
		// TODO: Check how this looks with curve correction
		final float interpolate = (0.5f * (direction.y + 1.0f)); // ^0.5f;
		return Color.linearBlend(sky, Color.white(), interpolate);
	}

	/**
	 * This function performs the actual ray casting of our ray tracer. It will
	 * query for the nearest collision, and blend colors recursively.
	 * 
	 * @param ray
	 *            The initial ray to consider in the algorithm.
	 * @param window
	 *            The range in which we consider ray-object collisions
	 * @param depth
	 *            The number of recursive steps our ray will take before being
	 *            absorbed or reaching the sky
	 * @return The combined colors our ray detected
	 * @see <a href="https://en.wikipedia.org/wiki/Ray_casting">Wikipedia: Ray
	 *      Casting</a>
	 */
	public Color castRay(final Ray ray, final Range<Float> window, final int depth) {
		// Base case; try changing the color and seeing what you get!
		if (depth <= 0)
			return Color.black();

		// Check to see if our ray hits an object, or just shoots into the sky
		final Intersection nearest = findNearest(ray, window);

		// If we do not hit anything, return our sky color.
		if (nearest == null)
			return skyBox(ray.direction);

		// Get the color of that object and the bounce vector for recursion if
		// there is recursion
		final Color sample = nearest.material.color;
		final Ray bounce = nearest.material.scatter(ray, nearest.point, nearest.normal, refractionIndex);

		// If the ray is absorbed for any reason while scattering, return black
		if (bounce == null)
			return Color.black();

		// Finally, we blend colors recursively
		Range<Float> newWindow = new Range<Float>(window.lower, window.upper - nearest.distance);

		return Color.mix(sample, castRay(bounce, newWindow, depth - 1));
	}

	/**
	 * Given a ray, this checks all the {@link Shape shapes} in our list of
	 * {@link Scene#things things} to see if our ray collides with it.
	 * 
	 * @param ray
	 *            The ray to check for an intersection
	 * @param window
	 *            The range in which we check for collisions
	 * @return The nearest detected intersection
	 */
	private Intersection findNearest(final Ray ray, final Range<Float> window) {
		// total_rays += 1;

		Intersection nearest = null;
		Range<Float> currentWindow = window;

		// This is a brute force check of every object in the scene
		for (Shape shape : things) {
			// If our ray has hit something already, reduce the window to check
			// for new collisions
			if (nearest != null)
				currentWindow = new Range<Float>(window.lower, nearest.distance);

			Intersection candidate = shape.intersectRay(ray, currentWindow);

			// Update our value of the nearest object if a new collision was
			// found inside the window
			if (candidate != null)
				nearest = candidate;
		}

		return nearest;
	}
}
