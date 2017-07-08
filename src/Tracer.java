import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Utility.GridTimer;
import Utility.Vector;

public class Tracer {

	public static void main(String[] args) {
		GridTimer t = new GridTimer(System.out);
		
		t.eventStart("Set up the Scene");

		@SuppressWarnings("unused")
		Scene s = new Scene();

		t.eventStop("Set up the Scene");
		t.eventStart("Set up the Camera");

		Camera c = new Camera(new Vector(0, 0, 5), new Vector(1, 0, 0), 400, 200, 8, 5, 90);

		t.eventStop("Set up the Camera");

		c.setTimer(t);

		try {
			// retrieve image
			BufferedImage bi = c.testCapture();

			t.eventStart("Writing image to file");

			File outputfile = new File("saved.png");
			ImageIO.write(bi, "png", outputfile);

			t.eventStop("Writing image to file");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
