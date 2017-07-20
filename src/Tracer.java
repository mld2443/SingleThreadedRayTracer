import java.io.IOException;

import tracer.Engine;
import tracer.Engine.SceneFormattingException;
import tracer.utils.GridTimer;

public class Tracer {

	public static void main(String[] args) {
		GridTimer t = new GridTimer(System.out);
		
		Engine engine;
		
		try {
			// Declares the file to load, width, height, samples per pixel,
			// depth per sample, timer
			engine = new Engine("example.scene", 960, 540, 10, 10, t);
			//engine = new Engine("example.scene", 1920, 1080, 80, 10, t);

			// A preview is much faster than capturing, rendering all shapes as
			// solid and only sampling each virtual pixel once
			//engine.savePreviewTo("preview.png");

			// Capture the image using regular ray-casting
			engine.saveCaptureTo("capture.png");
		} catch (SceneFormattingException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
