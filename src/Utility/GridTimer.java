/**
 * 
 */
package Utility;

import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * 
 * 
 * @author mld2443
 */
public class GridTimer implements GridTimerDelegate {
	/**
	 * A simple object for keeping track of when an event starts, when it stops, how long that took, and if it has completed.
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
		 * Concludes the timing of an event and calculates how long it took to complete
		 */
		public void finish() {
			if (completed) {
				// TODO: Raise an error; can't finish an event more than once
				return;
			}

			stopTime = System.nanoTime();
			elapsedTime = stopTime - startTime;
			completed = true;
		}
	}
	
	/**
	 * Spartan {@link Comparator} for {@link Event}
	 * 
	 * @author mld2443
	 */
	class EventCompare implements Comparator<Event> {

	    @Override
	    public int compare(Event e1, Event e2) {
	        return Long.compare(e1.startTime, e2.startTime);
	    }
	}

	private Hashtable<String, Event> events;
	private int width, height;
	private Event[][] grid;

	/**
	 * 
	 */
	public GridTimer() {
		this.events = new Hashtable<String, Event>();
	}

	@Override
	public void eventStart(final String eventName) {
		if (events.containsKey(eventName)) {
			// TODO: raise an error; duplicate event
			return;
		}

		events.put(eventName, new Event());
	}

	@Override
	public void eventStop(final String eventName) {
		Event e = events.get(eventName);

		if (e == null)
			// TODO: raise an error; event not found / never started
			return;

		e.finish();
	}

	@Override
	public void setGridSize(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.grid = new Event[width][height];
	}

	@Override
	public void gridEventStart(final int x, final int y) {
		grid[x][y] = new Event();
	}

	@Override
	public void gridEventStop(final int x, final int y) {
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
		// Taking the root of the values makes the intermediate colors look less
		// grey
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
	 * Calculate a colorized version of how long each event in the grid took.
	 * This is used to generate a heatmap image.
	 * 
	 * @return 2D array of integer color values
	 */
	private int[][] getColorGrid() {
		long low = grid[0][0].elapsedTime, high = low;

		// Find the largest and smallest values in the grid
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				long element = grid[x][y].elapsedTime;

				if (element > high)
					high = element;
				else if (element < low)
					low = element;
			}
		}

		// calculate the distance between the two extremes
		final double span = (double) (high - low);

		// This is where we will put the ultimate values
		int[][] colorGrid = new int[width][height];

		// Compute the color for each grid location
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Calculate the interpolation value of this grid location
				double interpolation = ((double) (grid[x][y].elapsedTime - low)) / span;

				// Apply the associated color to this location
				colorGrid[x][y] = colorScale(interpolation);
			}
		}

		return colorGrid;
	}

	@Override
	public void getEvents(PrintStream stream) {
		List<Event> elements = Collections.list(events.elements());
		
		Collections.sort(elements, new EventCompare());
		
		for (Event e : elements) {
			
		}
	}

	@Override
	public BufferedImage gridHeatmap() {
		final int[][] colorGrid = getColorGrid();

		BufferedImage heatmap = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				heatmap.setRGB(x, y, colorGrid[x][y]);
			}
		}

		return heatmap;
	}

}
