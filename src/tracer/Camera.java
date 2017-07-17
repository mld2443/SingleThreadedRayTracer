package tracer;
import java.awt.image.BufferedImage;
import java.util.Random;

import utilities.Color;
import utilities.GridTimerDelegate;
import utilities.Range;
import utilities.Ray;
import utilities.Vector;

/**
 * A camera obscura, which can set up inside a virtual scene and capture an
 * image using ray casting.
 * 
 * @author mld2443
 * @see <a href="https://en.wikipedia.org/wiki/Camera_obscura">Wikipedia: Camera
 *      Obscura</a>
 * @see <a href="https://en.wikipedia.org/wiki/Ray_casting">Wikipedia: Ray
 *      Casting</a>
 */
public class Camera {
	/**
	 * A static Random object with which to generate random floats [0,1).
	 */
	private static final Random rand = new Random();

	/**
	 * The location of the Camera in space.
	 */
	public final Vector position;

	/**
	 * The resolution of the Camera in pixels. Be sure to call
	 * {@link Camera#aimCamera} after modifying.
	 */
	public int width, height;

	/**
	 * The number of samples taken per pixel. The higher the number the better
	 * the quality.
	 */
	public int sampling;

	/**
	 * The maximum number of times a single sample ray will bounce.
	 */
	public int depth;

	/**
	 * The range inside which a ray will detect a hit.
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Viewing_frustum">Wikipedia:
	 *      Viewing Frustum</a>
	 */
	public Range<Double> frustum;

	/**
	 * Dimensions of a single pixel in 3D space relative to the camera position
	 */
	private Vector iHat, jHat;

	/**
	 * The top left of the Camera obscura.
	 */
	private Vector origin;

	/**
	 * The pixel array on which we capture an image.
	 */
	private int[][] film;

	/**
	 * Timer delegate; If a timer is given, {@link Camera} will report its
	 * actions to it for timing.
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Delegation_pattern">
	 *      Wikipedia: Delegation Pattern</a>
	 */
	private GridTimerDelegate timer;

	/**
	 * Sets up a new camera object and places it in a virtual scene.
	 * 
	 * @param position
	 *            The position of the new Camera
	 * @param direction
	 *            The direction the virtual Camera faces
	 * @param width
	 *            The width of the resolution in pixels
	 * @param height
	 *            The height of the resolution in pixels
	 * @param sampling
	 *            The number of samples taken per pixel
	 * @param depth
	 *            The maximum number of times a single sample will bounce
	 * @param FOV
	 *            The Field of View for the camera
	 * @param maxRange
	 *            The range of our camera, beyond which is sky.
	 */
	Camera(final Vector position, final Vector direction, final int width, final int height, final int sampling,
			final int depth, final double FOV) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.sampling = sampling;
		this.depth = depth;
		this.frustum = new Range<Double>(0.1, 1000.0);

		aimCamera(FOV, direction, new Vector(0.0f, 0.0f, 1.0f));
	}

	/**
	 * Calculates the camera's coordinate frame and origin point, as well as
	 * setting up the film to capture an image with.
	 * 
	 * @param FOV
	 *            The Field of View for the camera. Should be smaller than about
	 *            170 degrees. Smaller numbers are equivalent to a higher zoom
	 * @param direction
	 *            The direction to face before capture
	 * @param up
	 *            The upward direction, usually <0,0,1>
	 */
	public void aimCamera(final double FOV, final Vector direction, final Vector up) {
		final Vector unitDirection = direction.normalize();

		// Calculate the screen dimensions given the FOV
		// These are actually at 1/2 factor, but that's useful later
		final double screenWidth = Math.tan(Math.toRadians(FOV / 2.0f));
		final double screenHeight = (((double) height) / ((double) width)) * screenWidth;

		// Calculate the coordinate frame for screenspace
		final Vector iStar = Vector.cross(up, unitDirection).normalize();
		final Vector jStar = Vector.cross(iStar, unitDirection).normalize();

		// Compute the dimensions of a pixel represented in screenspace
		this.iHat = iStar.scale(2 * screenWidth / (double) width);
		this.jHat = jStar.scale(2 * screenHeight / (double) height);

		// The top left of the screenspace is the origin of our image
		this.origin = Vector.sum(unitDirection, iStar.scale(-screenWidth), jStar.scale(-screenHeight));

		this.film = new int[width][height];

		if (timer != null)
			this.timer.setGridSize(width, height);
	}

	/**
	 * Sets the timer delegate and updates its necessary parameters.
	 * 
	 * @param timer
	 *            The new timer delegate
	 * @see GridTimerDelegate
	 */
	public void setTimer(GridTimerDelegate timer) {
		this.timer = timer;
		this.timer.setGridSize(width, height);
	}

	/**
	 * Getter for the timer delegate.
	 * 
	 * @return The current timer if there is one
	 */
	public GridTimerDelegate getTimer() {
		return timer;
	}

	/**
	 * Takes our color values from {@link Camera#film} and puts them into a
	 * {@link BufferedImage}. This complexity of this function is dwarfed by the
	 * ray casting algorithm itself, so it doesn't matter much how fast this is.
	 * 
	 * @return A {@link BufferedImage} ready to be output to a file.
	 */
	private BufferedImage developFilm() {
		if (timer != null)
			timer.eventStart("Develop Scene");

		// Create an empty RGB Image
		BufferedImage negative = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Transfer the color values to the Image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				negative.setRGB(x, y, film[x][y]);
			}
		}

		if (timer != null)
			timer.eventStop("Develop Scene");

		return negative;
	}

	/**
	 * Fast scene rendering intended to help with staging a {@link Scene}. This
	 * method samples each pixel only once and returns the Color of the nearest
	 * object without bouncing or supersampling.
	 * 
	 * This method does not use {@link Camera#depth} or {@link Camera#sampling}.
	 * 
	 * @param scene
	 *            Scene to preview
	 * @return An {@link BufferedImage} with a simple preview
	 */
	public BufferedImage preview(final Scene scene) {
		if (timer != null)
			timer.eventStart("Preview Scene");

		// Iterate over ever pixel on our virtual screen
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (timer != null)
					timer.gridEventStart(x, y);

				// Calculate the looking direction
				final Vector screenSpacePosition = Vector.sum(origin, iHat.scale(0.5f + x), jHat.scale(0.5f + y));
				final Ray cast = new Ray(position, screenSpacePosition);

				// Retrieve and apply the color
				this.film[x][y] = scene.preview(cast, frustum).quantize();

				if (timer != null)
					timer.gridEventStop(x, y);
			}
		}

		if (timer != null)
			timer.eventStop("Preview Scene");

		return developFilm();
	}

	/**
	 * The ultimate function of the Camera, this function performs the actual
	 * ray casting. If this were a real camera, this function would be the
	 * shutter button. The casting is done in a scanline manner, left to right,
	 * then top to bottom.
	 * 
	 * @param scene
	 *            The scene to capture
	 * @return A {@link BufferedImage} containing the captured scene
	 */
	public BufferedImage captureScene(final Scene scene) {
		if (timer != null)
			timer.eventStart("Capture Scene");

		// Iterate over every pixel on our virtual screen
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (timer != null)
					timer.gridEventStart(x, y);

				this.film[x][y] = getPixel(scene, x, y).quantize();

				if (timer != null)
					timer.gridEventStop(x, y);
			}
		}

		if (timer != null)
			timer.eventStop("Capture Scene");

		return developFilm();
	}

	/**
	 * Helper method to sample our scene multiple times defined by
	 * {@link Camera#sampling} and average the value. It constructs the
	 * {@link Ray} using the screen space coordinate vectors.
	 * 
	 * @param scene
	 *            Scene to sample
	 * @param x
	 *            X coordinate on our virtual screen
	 * @param y
	 *            Y coordinate on our virtual screen
	 * @return Average {@link Color} of this pixel
	 */
	private Color getPixel(Scene scene, final int x, final int y) {
		Color pixel = Color.black();

		// Collect samples of the scene for this current pixel
		for (int s = 0; s < sampling; s++) {
			// Randomly generate offsets for the current subsample
			final double xCoord = x + rand.nextDouble();
			final double yCoord = y + rand.nextDouble();

			// Get the subsample position and construct a ray from it
			final Vector screenSpacePosition = Vector.sum(origin, iHat.scale(xCoord), jHat.scale(yCoord));
			final Ray cast = new Ray(position, screenSpacePosition);

			pixel = Color.add(pixel, scene.castRay(cast, frustum, depth));
		}

		// Color correction
		pixel = pixel.reduce(sampling);
		
		// This brightens the image
		//pixel = pixel.applyTransform(v -> Math.sqrt(v));
		
		// This darkens the image
		//pixel = pixel.applyTransform(v -> v*v);

		return pixel;
	}
}
