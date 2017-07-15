import java.io.IOException;

import tracer.Engine;
import tracer.Engine.SceneFormattingException;
import utilities.GridTimer;

public class Tracer {

	public static void main(String[] args) {
		GridTimer t = new GridTimer(System.out);
		
		Engine engine;
		
		try {
			engine = new Engine("example.scene", 800, 400, 100, 8, t);
			
			engine.saveCaptureTo("capture.png");
		} catch (SceneFormattingException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
