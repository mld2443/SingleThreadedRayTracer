package tracer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Reads in a file for a scene.
 * 
 * @author mld2443
 */
public class Engine {
	@SuppressWarnings("serial")
	public class SceneFormattingException extends RuntimeException {
		public SceneFormattingException(String message) {
			super(message);
		}
	}
	
	Scene scene;
	
	/**
	 * Default constructor.
	 */
	public Engine() {
	}
	
	public void load(String filename) throws IOException {
		LinkedList<String> lines = new LinkedList<>();
		
		Stream<String> lineStream = Files.lines(Paths.get(filename));
		
		lineStream.forEach(lines::add);
		
		lineStream.close();
				
		for (String line : lines) {
			// remove comments
			line = line.split("//", 2)[0];
			
			// ignore lines without instructions
			if (line.trim().equals(""))
				continue;
			
			
		}
	}

}
