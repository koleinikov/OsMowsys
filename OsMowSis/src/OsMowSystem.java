

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
/**
 * 
 * @author koleinikov3
 *
 */
public class OsMowSystem {

	// @TODO you have to always code against interface, so you have to use List
	// instead of ArrayList
	private List<Mower> mowers = new ArrayList<>();
	private SystemMap map;
	private int numberOfTurns;
	private int currentTurn = 0;
	private final static String CODE_OK = "ok";
	private static final String CODE_CRASH = "crash";

	private static OsMowSystem singleton;

	// private default constructor
	private OsMowSystem() {

	}

	// @TODO the whole idea of have static methods and members are really bad. That
	// is the reason why we are doing SINGLETON patter here.
	// So the MOWER class have a chance to get hold on the instance of the
	// OsMowSystem and invoke its method. It's not the perfect design, of course.
	// After all the Mower is coupled with the OsMowSystem, but it is way better
	// than static method and members invocation.
	public static OsMowSystem getInstance() {
		if (singleton == null) {
			singleton = new OsMowSystem();
		}
		return singleton;
	}

	/**
	 * Initializes the system.
	 * 
	 * @param input
	 *            File or User Input
	 */
	public void init(List<String> input) {
		int counter = 0;
		// @TODO validate all data first and perform the init only if the data input
		// format is valid.
		int width = Integer.parseInt(input.get(counter++));
		int height = Integer.parseInt(input.get(counter++));
		this.map = new SystemMap(width, height);
		int mowerCount = Integer.parseInt(input.get(counter++));
		int id = 0;
		for (int i = 3; i <= 2 + mowerCount; i++) {
			counter++;
			String mowerString = input.get(i);
			String[] params = mowerString.split(",", 0);
			int strategyId = Integer.parseInt(params[3].trim());
			Mower mower = new Mower(params[2].trim(), id++, strategyId);
			this.map.addMower(Integer.parseInt(params[0].trim()), Integer.parseInt(params[1].trim()), mower);
			mowers.add(mower);
		}
		int craterCount = Integer.parseInt(input.get(counter++));
		final int constant = counter;
		ArrayList<Crater> craters = new ArrayList<Crater>();
		for (int i = constant; i < constant + craterCount; i++) {
			counter++;
			String craterString = input.get(i);
			String[] params = craterString.split(",", 0);
			Crater crater = new Crater();
			this.map.addCrater(Integer.parseInt(params[0].trim()), Integer.parseInt(params[1].trim()), crater);
			craters.add(crater);

		}
		this.map.fillGrass();
		this.numberOfTurns = Integer.parseInt(input.get(counter++));

		// System.out.println(map);

	}

	/**
	 * Gets the list of mowers
	 * 
	 * @return
	 */
	public List<Mower> getMowers() {
		return this.mowers;
	}

	/**
	 * Returns the array of System Objects.
	 * 
	 * @param m
	 * @return
	 */
	public SystemMapObject[] scan(Mower m) {
		SystemMapObject[] result = null;
		// @TODO override equals and hash code for the Mower, so we could use contains
		// method, instead of the iteration.
		if (mowers.contains(m)) {
			result = map.scan(m);

		}
		return result;
	}

	// @TODO we have to determine Crash or Not Crash
	// we have to create output.
	void processDecision(Mower mower, SystemMessage decision) {
		String rawMsg = decision.getMessage();

		ACTION action = ACTION.valueOf(rawMsg);

		switch (action) {
		case SCAN: {
			System.out.println(String.format("m%d,%s", mower.getId(), action.name().toLowerCase()));
			SystemMapObject[] results = scan(mower);
			String line = Arrays.asList(results).toString().replaceAll(", ", ",");
			line = line.substring(1, line.length() - 1);
			System.out.println(line);
			break;
		}
		case MOVE: {
			System.out.println(String.format("m%d,%s", mower.getId(), action.name().toLowerCase()));
			String code;
			if (!isValidDirection(mower)) {
				code = CODE_CRASH;
				mower.setCrashed(Boolean.TRUE);
				this.map.removeMower(mower);

			} else {
				map.moveMower(mower);
				code = CODE_OK;
			}
			System.out.println(code);
			break;
		}
		case STEER: {
			// let's get direction
			@SuppressWarnings("unused")
			DIRECTION direction = mower.getDirection();
			System.out.println(String.format("m%d,%s,%s", mower.getId(), action.name().toLowerCase(),
					mower.getDirection().name().toLowerCase()));
			String code = CODE_OK;
			// if (!isValidDirection(mower)) {
			// code = CODE_CRASH;
			// mower.setCrashed(Boolean.TRUE);
			// }
			System.out.println(code);
			break;
		}
		case PASS: {
			System.out.println(String.format("m%d,%s", mower.getId(), action.name().toLowerCase()));
			System.out.println(CODE_OK);
			break;
		}
		}

	}

	// @TODO find if the direction is valid
	Boolean isValidDirection(Mower mower) {
		SystemMapObject[] squares = scan(mower);
		Map<DIRECTION, SystemMapObject> directionMap = new HashMap<DIRECTION, SystemMapObject>();
		int index = 0;
		for (DIRECTION direction : DIRECTION.values()) {
			directionMap.put(direction, squares[index++]);
		}
		Boolean isValid = directionMap.get(mower.getDirection()).causesCrash() ? Boolean.FALSE : Boolean.TRUE;
		return isValid;
	}

	// @TODO single line of 4 numbers, separated by comma: total number of
	// squares,original number of grassed squares (including mower),number of cut
	// grassed,total number of turns.
	void finalReport() {
		System.out.println(String.format("%d,%d,%d,%d", map.getTotalNumberOfSquares(),
				map.getOriginalNumberOfGrassSquares(), map.getNumberOfCutGrassSquares(), currentTurn));
	}

	/**
	 * runs the simulation to completion
	 */
	public void run() {
		// @TODO let's check that at least one mower is active and simulation duration
		// is not exceeded.
		try {
			Predicate<Mower> crashedMower = Mower::getCrashed;
			// now use predicate, we do have at least one mower that is OK and we have grass
			// to mow and we
			// still do have some turns to make
			while (mowers.stream().anyMatch(crashedMower.negate()) && !map.isMowed() && currentTurn < numberOfTurns) {
				currentTurn++;
				mowers.stream().filter(crashedMower.negate()).forEach(m -> {
					SystemMessage decision = m.decide();
					processDecision(m, decision);

				});
			}
		} catch (Exception e) {
		} finally {
			finalReport();
		}

		// System.out.println(map);

	}
}
