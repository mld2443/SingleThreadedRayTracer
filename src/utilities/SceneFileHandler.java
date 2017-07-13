package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Stream;

import shapes.Shape;

/**
 * Reads in a file for a scene.
 * 
 * @author mld2443
 */
public class SceneFileHandler {
	@SuppressWarnings("serial")
	public class SceneFormattingException extends RuntimeException {
		public SceneFormattingException(String message) {
			super(message);
		}
	}
	
	LinkedList<Shape> shapes;
	
	/**
	 * Default constructor.
	 */
	public SceneFileHandler() {
		shapes = new LinkedList<>();
	}
	
	public LinkedList<Shape> readFile(String filename) throws IOException {
		LinkedList<String> lines = new LinkedList<>();
		
		Stream<String> lineStream = Files.lines(Paths.get(filename));
		
		lineStream.forEach(lines::add);
		
		lineStream.close();
				
		for (String line : lines) {
			if (line == "" || line.charAt(0) == '#')
				continue;
			
			
		}
		
		return shapes;
	}

}
