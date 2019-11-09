
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * 
 * @author koleinikov3
 *
 */
public class InputParser {
	/**
	 * This method is to take in a filePath and return an optional list of lines.
	 * 
	 * @param String
	 *            the path of the input file
	 * @return optional list of lines. so if there was an error, the optional instance would not have data present
	 */
	public Optional<List<String>> parse(String filePath) {
		Optional<List<String>> result = Optional.ofNullable(null);
		
		//read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			return result = Optional.of(stream.collect(Collectors.toList()));

		} catch (IOException e) {
			System.err.println("Error parsing input file");
		}

		return result;
	}
}
