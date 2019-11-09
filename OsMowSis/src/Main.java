

import java.util.List;
import java.util.Optional;
/**
 * 
 * @author koleinikov3
 *
 */
public class Main {
	public static void main(String[] args) {

		// check for the test scenario file name
		// > java -jar osmowsis.jar <name of scenario file>
		if (args.length < 1) {
			System.out.println("ERROR: Test scenario file name not found.");
			return;
		}

		InputParser parser = new InputParser();
		// relative path to file is taken fromn the argument
		Optional<List<String>> inputs = parser.parse(args[0]);
		if (inputs.isPresent()) {
			// parser worked correctly
			List<String> input = inputs.get();
			// extract lines from optional instance
			OsMowSystem systemOsMow = OsMowSystem.getInstance();
			// get singleton
			systemOsMow.init(input);
			// setup initial state for the simulation run
			systemOsMow.run();
			// run simulation
		}

	}
}
