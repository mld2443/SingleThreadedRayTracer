//import java.io.File;
import java.io.IOException;
//import java.io.PrintStream;

//import javax.imageio.ImageIO;

import tracer.Engine;
import tracer.Engine.SceneFormattingException;
import tracer.utils.GridTimer;

public class Tracer {

	public static void main(String[] args) {
		GridTimer t = new GridTimer(System.out);
		
		Engine engine;
		
		try {
			// This would output timing info to a file instead of System.out
			//t.logger = new PrintStream(new File("log.txt"));
			
			// Declares the file to load, width, height, samples per pixel,
			// depth per sample, timer
			engine = new Engine("example.scene", 960, 540, 10, 10, t);
			//engine = new Engine("example.scene", 1920, 1080, 80, 10, t);

			// A preview is much faster than capturing, rendering all shapes as
			// solid and only sampling each virtual pixel once
			//engine.savePreviewTo("preview.png");

			// Capture the image using regular ray-casting
			engine.saveCaptureTo("capture.png");
			
			System.out.println("Measured Speedup: " + t.calculateSpeedup());
			
			// Saves a normalized heatmap of how long each pixel took to render
			//ImageIO.write(t.gridHeatmap(), "PNG", new File("heatmap.png"));
		} catch (SceneFormattingException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
