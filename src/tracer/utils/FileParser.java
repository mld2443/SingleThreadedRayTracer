package tracer.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A class containing only static methods and classes pertaining to the
 * extraction of formatted information from a file.
 * 
 * @author mld2443
 */
public class FileParser {
	/**
	 * A single highest-level entry in a file for the scene format.
	 * 
	 * @author mld2443
	 */
	public static class Entry {
		/**
		 * The type of object that was defined and a name if given.
		 */
		public String type, name;

		/**
		 * Secondary properties used to define this objects traits.
		 */
		public Map<String, String> properties;

		/**
		 * The default constructor.
		 * 
		 * @param type
		 *            The type of object being defined by this Entry
		 */
		public Entry(final String type) {
			this.type = type;
			this.name = "";
			properties = new HashMap<>();
		}
	}

	/**
	 * Extracts formatted information from a file.
	 * 
	 * @param filename
	 *            The name of the file to open
	 * @return A {@link LinkedList} of {@link Entry Entries}
	 * @throws IOException
	 */
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
