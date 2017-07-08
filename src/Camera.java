import java.awt.image.BufferedImage;

import Utility.Color;
import Utility.GridTimerDelegate;
import Utility.Range;
import Utility.Vector;

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
	 * The location of the Camera relative to the {@link Camera#origin}.
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
	public Range<Float> frustum;

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
			final int depth, final int FOV) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.sampling = sampling;
		this.depth = depth;
		this.frustum = new Range<Float>(0.1f, 1000.0f);

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
	public void aimCamera(final int FOV, final Vector direction, final Vector up) {
		final Vector unitDirection = direction.normalize();

		// Calculate the screen dimensions given the FOV
		// These are actually at 1/2 factor, but that's useful later
		final float screenWidth = (float) Math.tan(Math.toRadians(FOV / 2.0f));
		final float screenHeight = (((float) height) / ((float) width)) * screenWidth;

		// Calculate the coordinate frame for screenspace
		final Vector iStar = Vector.cross(unitDirection, up).normalize();
		final Vector jStar = Vector.cross(unitDirection, iStar).normalize();

		// Compute the dimensions of a pixel represented in screenspace
		this.iHat = Vector.scale(iStar, (2 * screenWidth / width));
		this.jHat = Vector.scale(jStar, (2 * screenHeight / height));

		// The top left of the screenspace is the origin of our image
		this.origin = Vector.add(Vector.add(unitDirection, Vector.scale(iStar, screenWidth)),
				Vector.scale(jStar, screenHeight));

		this.film = new int[width][height];
		
		if (timer != null)
			this.timer.setGridSize(width, height);
	}
	
	/**
	 * Sets the timer delegate and updates its necessary parameters.
	 * 
	 * @param timer The new timer delegate
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

		BufferedImage negative = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

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
	 * The ultimate function of the Camera, this function performs the actual
	 * ray casting. If this were a real camera, this function would be the
	 * shutter button.
	 * 
	 * @param scene
	 *            The scene to capture
	 * @return A {@link BufferedImage} containing the captured scene
	 */
	public BufferedImage captureScene(final Scene scene) {
		if (timer != null)
			timer.eventStart("Capture Scene");

		for (int y = 0; y < height; y++) {
			// Keep track of the scanline position
			Vector deltaY = Vector.add(origin, Vector.scale(jHat, y));

			for (int x = 0; x < width; x++) {
				// This is the top-left of the pixel window
				@SuppressWarnings("unused")
				Vector pixelPosition = Vector.add(deltaY, Vector.scale(iHat, x));

				this.film[x][y] = new Color(0.0f, 0.5f, 0.0f).quantize();
			}
		}

		if (timer != null)
			timer.eventStop("Capture Scene");

		return developFilm();
	}

	/**
	 * Test function designed to test the image conversion technique.
	 * 
	 * @deprecated
	 * @return A {@link BufferedImage} containing a color gradient
	 */
	public BufferedImage testCapture() {
		if (timer != null)
			timer.eventStart("Capture test image");

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float interpolatedX = ((float) x) / ((float) width);
				float interpolatedY = ((float) y) / ((float) height);

				this.film[x][y] = new Color(interpolatedX, 0.5f, interpolatedY).quantize();
			}
		}

		if (timer != null)
			timer.eventStop("Capture test image");

		return developFilm();
	}
}
