package tracer.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class FileParser {
	public static class Entry {
		public String type, name;
		public Map<String, String> properties;
		
		public Entry(final String type) {
			this.type = type;
			this.name = "";
			properties = new HashMap<>();
		}
	}

	public static List<Entry> parse(final String filename) throws IOException {
		List<String> lines = new LinkedList<>();
		
		// Add the lines to a list of strings
		Stream<String> lineStream = Files.lines(Paths.get(filename));
		lineStream.forEach(lines::add);
		lineStream.close();
		
		List<Entry> definitions = new LinkedList<>();
				
		for (String line : lines) {
			// remove comments
			line = line.split("//", 2)[0];
			
			// ignore lines without instructions
			if (line.trim().equals(""))
				continue;
			
			if (line.startsWith("  ")) {
				// This line defines a property
				
				if (definitions.isEmpty())
					throw new IOException("Improper Scene file formatting.");
				
				String[] property = line.trim().split(" ", 2);
				
				definitions.get(definitions.size() - 1).properties.put(property[0], property[1]);
			} else {
				// This line defines a new entry
				
				String[] entry = line.split(" ", 2);
				definitions.add(new Entry(entry[0]));
				
				if (entry.length == 2)
					definitions.get(definitions.size() - 1).name = entry[1];
			}
		}
		
		return definitions;
	}

}
