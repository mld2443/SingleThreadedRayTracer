import Utility.Color;
import Utility.Vector;

public class Camera {
	Color[][] film;
	Vector Position;
	
	Camera(final int width, final int height) {
		film = new Color[height][width];
	}
}
