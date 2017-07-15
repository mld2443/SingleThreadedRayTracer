package utilities;

import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.util.Hashtable;

/**
 * 
 * 
 * @author mld2443
 */
public class GridTimer implements GridTimerDelegate {
	@SuppressWarnings("serial")
	public class TimerEventException extends RuntimeException {
		public TimerEventException(String message) {
			super(message);
		}
	}
	
	/**
	 * A simple object for keeping track of when an event starts, when it stops,
	 * how long that took, and if it has completed.
	 * 
	 * @author mld2443
	 */
	private class Event {
		public final long startTime;
		public long stopTime;
		public long elapsedTime;
		public boolean completed;

		/**
		 * Starts timing an event
		 */
		public Event() {
			completed = false;
			startTime = System.nanoTime();
		}

		/**
		 * Concludes the timing of an event and calculates how long it took to
		 * complete
		 */
		public void finish() {
			stopTime = System.nanoTime();
			elapsedTime = stopTime - startTime;
			completed = true;
		}
	}

	/**
	 * A delegate for logging the events as they finish
	 */
	public PrintStream logger;

	private Hashtable<String, Event> events;
	private int width, height;
	private Event[][] grid;

	/**
	 * Default constructor. This does not set up the actual grid, be sure to
	 * call {@link GridTimer#setGridSize(int, int)}.
	 */
	public GridTimer() {
		this.events = new Hashtable<String, Event>();
	}

	/**
	 * Optional constructor to set the logger delegate. This does not set up the
	 * actual grid, be sure to call {@link GridTimer#setGridSize(int, int)}.
	 * 
	 * @param stream
	 *            The {@link PrintStream} to use as the logger
	 */
	public GridTimer(PrintStream stream) {
		this.events = new Hashtable<String, Event>();
		this.logger = stream;
	}

	@Override
	public void eventStart(final String eventName) throws TimerEventException {
		// Catch some simple exceptions
		if (events.containsKey(eventName))
			throw new TimerEventException("Event with the name \"" + eventName + "\" already exists.");
		
		events.put(eventName, new Event());
	}

	@Override
	public void eventStop(final String eventName) throws TimerEventException {
		Event e = events.get(eventName);
		
		// Catch some simple exceptions
		if (e == null)
			throw new TimerEventException("Event \"" + eventName + "\" is not found.");
		if (e.completed)
			throw new TimerEventException("Event \"" + eventName + "\" is already stopped.");
		
		e.finish();

		// If the logger delegate is set, output to the logger
		if (logger != null) {
			final double seconds = ((double) e.elapsedTime) / 1_000_000_000.0;

			logger.println(String.format("%s:\t%.3g s", eventName, seconds));
		}
	}

	@Override
	public void setGridSize(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.grid = new Event[width][height];
	}

	@Override
	public void gridEventStart(final int x, final int y) throws TimerEventException {
		// Catch some simple exceptions
		if (grid[x][y] != null)
			throw new TimerEventException("Event at Grid[" + x + "][" + y + "] already exists.");
		
		grid[x][y] = new Event();
	}

	@Override
	public void gridEventStop(final int x, final int y) throws TimerEventException {
		// Catch some simple exceptions
		if (grid[x][y] == null)
			throw new TimerEventException("Event at Grid[" + x + "][" + y + "] was not started.");
		if (grid[x][y].completed)
			throw new TimerEventException("Event at Grid[" + x + "][" + y + "] is already stopped.");
		
		grid[x][y].finish();
	}

	/**
	 * Computes a red or cyan color value based on a simple interpolation
	 * between 0.0 and 1.0.
	 * 
	 * @param interpolation
	 *            A value between 0.0 and 1.0 of how red the result should be
	 * @return A bit-shifted color ready to be put into a {@link BufferedImage}
	 */
	private int colorScale(final double interpolation) {
		// Curving by taking the root of the values makes the intermediate
		// colors look less grey
		final double curve = 1.0 / 3.0;

		// Compute the curved values as integers
		final int curvedValue = (int) (255 * Math.pow(interpolation, curve));
		final int curvedInvValue = (int) (255 * Math.pow(1 - interpolation, curve));

		// Clip any extreme values, should they exist
		final int red = Math.min(Math.max(curvedValue, 0), 255);
		final int cyan = Math.min(Math.max(curvedInvValue, 0), 255);

		// bit-shift to the final color
		return red << 16 | cyan << 8 | cyan;
	}

	/**
	 * Compiles the grid-based information into an output image.
	 * 
	 * @return A {@link BufferedImage} heatmap of the time each task in the grid
	 *         takes to complete ready to be output into a file
	 */
	public BufferedImage gridHeatmap() throws TimerEventException {
		// This action is not instantaneous, might as well time it
		eventStart("Generate Heatmap");

		// Instantiate the extremes
		long low = grid[0][0].elapsedTime, high = low;

		// Find the largest and smallest values in the grid
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (grid[x][y] == null)
					throw new TimerEventException("Grid[" + x + "][" + y + "] uninitialized.");
				
				final long element = grid[x][y].elapsedTime;

				if (element > high)
					high = element;
				else if (element < low)
					low = element;
			}
		}
		
		// calculate the distance between the two extremes
		final double span = (double) (high - low);

		// This is where we will put the ultimate values
		BufferedImage heatmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Compute the color for each grid location
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Calculate the interpolation value of this grid location
				double interpolation = ((double) (grid[x][y].elapsedTime - low)) / span;

				// Apply the associated color to this location
				heatmap.setRGB(x, y, colorScale(interpolation));
			}
		}

		// Clock in and see how we did
		eventStop("Generate Heatmap");

		return heatmap;
	}

}
