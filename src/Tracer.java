import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utilities.GridTimer;
import utilities.Vector;

public class Tracer {

	public static void main(String[] args) {
		GridTimer t = new GridTimer(System.out);
		
		t.eventStart("Set up the Scene");

		Scene s = new Scene();

		t.eventStop("Set up the Scene");
		t.eventStart("Set up the Camera");

		Camera c = new Camera(new Vector(0, 0, 5), new Vector(1, 0, 0), 800, 400, 100, 10, 90);

		t.eventStop("Set up the Camera");

		c.setTimer(t);

		try {
			// retrieve image
			BufferedImage bi = c.captureScene(s);

			t.eventStart("Writing image to file");

			File outputfile = new File("capture.png");
			ImageIO.write(bi, "png", outputfile);

			t.eventStop("Writing image to file");
			
			bi = t.gridHeatmap();
			
			t.eventStart("Write the heatmap");
			
			outputfile = new File("heatmap.png");
			ImageIO.write(bi, "png", outputfile);
			
			t.eventStop("Write the heatmap");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
