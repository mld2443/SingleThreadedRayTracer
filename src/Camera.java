import Utility.Color;
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
	 * {@link Camera#positionCamera} after modifying.
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
	 * Dimensions of a single pixel relative to the camera position
	 */
	private Vector iHat, jHat;

	/**
	 * The top left of the Camera obscura.
	 */
	private Vector origin;

	/**
	 * The pixels on which we capture an image.
	 */
	private Color[][] film;

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
			final int depth, final int FOV, final float maxRange) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.sampling = sampling;
		this.depth = depth;
		this.frustum = new Range<Float>(0.1f, maxRange);

		positionCamera(FOV, direction, new Vector(0.0f, 0.0f, 1.0f));
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
	public void positionCamera(final int FOV, final Vector direction, final Vector up) {
		final Vector unitDirection = direction.normalize();

		// Calculate the screen dimensions given the FOV
		// These are actually at 1/2 factor, but that's useful later
		final float screenWidth = (float) Math.tan(Math.toRadians(FOV / 2.0));
		final float screenHeight = (((float) height) / ((float) width)) * screenWidth;

		// Calculate the coordinate frame for screenspace
		final Vector iStar = Vector.cross(unitDirection, up).normalize();
		final Vector jStar = Vector.cross(iStar, unitDirection).normalize();

		// Compute the dimensions of a pixel represented in screenspace
		this.iHat = Vector.scale(iStar, (2 * screenWidth / width));
		this.jHat = Vector.scale(jStar, (2 * screenHeight / height));

		// The top left of the screenspace is the origin of our image
		this.origin = Vector.add(Vector.sub(unitDirection, Vector.scale(iStar, screenWidth)),
				Vector.scale(jStar, screenHeight));

		this.film = new Color[height][width];
	}
}
