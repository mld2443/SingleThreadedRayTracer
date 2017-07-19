import java.io.IOException;

import tracer.Engine;
import tracer.Engine.SceneFormattingException;
import tracer.utils.GridTimer;

public class Tracer {

	public static void main(String[] args) {
		GridTimer t = new GridTimer(System.out);
		
		Engine engine;
		
		try {
			engine = new Engine("example.scene", 960, 540, 20, 10, t);
			//engine = new Engine("example.scene", 1920, 1080, 80, 10, t);
			
			//engine.savePreviewTo("preview.png");
			engine.saveCaptureTo("capture.png");
		} catch (SceneFormattingException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
