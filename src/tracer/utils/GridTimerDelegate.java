package tracer.utils;

/**
 * A delegation interface for timing miscellaneous events as well as events
 * associated with a grid based activity.
 * 
 * @author mld2443
 */
public interface GridTimerDelegate {
	/**
	 * Begin the timing of an event.
	 * 
	 * @param eventName
	 *            The name of the event; this will be used to identify it in
	 *            {@link GridTimerDelegate#gridHeatmap()}
	 */
	public void eventStart(final String eventName);

	/**
	 * Logs the end of an event. This will log the event as having been
	 * completed, and how long it took to complete.
	 * 
	 * @param eventName
	 *            The name of the event; must match an already existing event in
	 *            progress
	 */
	public void eventStop(final String eventName);

	/**
	 * Initializes the grid for which we will put timing information for a
	 * 2-Dimensional grid based task.
	 * 
	 * @param width
	 *            Width of the grid
	 * @param height
	 *            Height of the grid
	 */
	public void setGridSize(final int width, final int height);

	/**
	 * Begins the clock of a task in the grid.
	 * 
	 * @param x
	 *            X position of the task
	 * @param y
	 *            Y position of the task
	 */
	public void gridEventStart(final int x, final int y);

	/**
	 * Stops the clock of a task in the grid.
	 * 
	 * @param x
	 *            X position of the task
	 * @param y
	 *            Y position of the task
	 */
	public void gridEventStop(final int x, final int y);
	
	public double calculateSpeedup();
}
