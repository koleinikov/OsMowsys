

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
/**
 * 
 * @author koleinikov3
 *
 */
public class BestMowerStrategy extends AbstractMowerStrategy {
	// @TODO
	private List<ArrayList<SystemMapObject>> map;
	private int x = 0;
	private int y = 0;
	private boolean alreadyScanned = false;
	private boolean randomSteer = false;
	private Random random = new Random();
	private boolean scanOnce = false;

	public BestMowerStrategy(Mower mower) {
		super(mower);
		map = new ArrayList<ArrayList<SystemMapObject>>();
		this.map.add(new ArrayList<SystemMapObject>());
		this.map.get(0).add(mower);
	}

	public boolean containsGrass() {
		boolean result = false;
		for (int i = 0; i < this.map.size(); i++) {
			for (int j = 0; j < this.map.get(i).size(); j++) {
				if (this.map.get(i).get(j) instanceof Grass) {
					result = true;
					break;
				}

			}
		}
		return result;
	}

	@Override
	public Entry<ACTION, DIRECTION> decide() {
		// @TODO here an algorithm should be created. For now just move to show the
		// concept.
		Entry<ACTION, DIRECTION> result = null;
		if ((!containsGrass() && !alreadyScanned) | scanOnce) {
			doScan();
			alreadyScanned = true;
			scanOnce = false;
			result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.SCAN, this.mower.getDirection());
		} else {
			// Move towards grass
			if (getFutureDirection() == null) {
				if (!randomSteer) {
					while (result == null) {
						int randInt = random.nextInt(8);
						// steer
						switch (DIRECTION.values()[randInt]) {
						case NORTH:
							if (y + 1 < this.map.size() && !this.map.get(y + 1).get(x).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER, DIRECTION.NORTH);

							}
							break;
						case SOUTH:
							if (y - 1 >= 0 && x < this.map.get(y - 1).size()
									&& !this.map.get(y - 1).get(x).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER, DIRECTION.SOUTH);

							}
							break;
						case EAST:
							if (x + 1 < this.map.get(y).size() && !this.map.get(y).get(x + 1).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER, DIRECTION.EAST);

							}
							break;
						case WEST:
							if (x - 1 >= 0 && !this.map.get(y).get(x - 1).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER, DIRECTION.WEST);

							}
							break;
						case NORTHEAST:
							if ((y + 1 < this.map.size() && x + 1 < this.map.get(y + 1).size())
									&& !this.map.get(y + 1).get(x + 1).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER,
										DIRECTION.NORTHEAST);
							}
							break;
						case NORTHWEST:
							if ((y + 1 < this.map.size() && x - 1 >= 0)
									&& !this.map.get(y + 1).get(x - 1).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER,
										DIRECTION.NORTHWEST);
							}
							break;
						case SOUTHEAST:
							if ((y - 1 >= 0 && x + 1 < this.map.get(y - 1).size())
									&& !this.map.get(y - 1).get(x + 1).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER,
										DIRECTION.SOUTHEAST);
							}
							break;
						case SOUTHWEST:
							if ((y - 1 >= 0 && x - 1 >= 0) && !this.map.get(y - 1).get(x - 1).causesCrash()) {
								result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER,
										DIRECTION.SOUTHWEST);
							}
							break;
						}
					}
					randomSteer = true;
					scanOnce = true;
				} else {
					// move
					if (this.mower.getDirection().equals(DIRECTION.EAST)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.EAST);
						this.map.get(y).set(x, new Empty());
						this.map.get(y).set(x + 1, this.mower);
						this.x = x + 1;
					} else if (this.mower.getDirection().equals(DIRECTION.WEST)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.WEST);
						this.map.get(y).set(x, new Empty());
						this.map.get(y).set(x - 1, this.mower);
						this.x = x - 1;
					} else if (this.mower.getDirection().equals(DIRECTION.NORTH)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.NORTH);
						this.map.get(y).set(x, new Empty());
						this.map.get(y + 1).set(x, this.mower);
						this.y = y + 1;
					} else if (this.mower.getDirection().equals(DIRECTION.SOUTH)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.SOUTH);
						this.map.get(y).set(x, new Empty());
						this.map.get(y - 1).set(x, this.mower);
						this.y = y - 1;
					} else if (this.mower.getDirection().equals(DIRECTION.NORTHEAST)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.NORTHEAST);
						this.map.get(y).set(x, new Empty());
						this.map.get(y + 1).set(x + 1, this.mower);
						this.y = y + 1;
						this.x = x + 1;
					} else if (this.mower.getDirection().equals(DIRECTION.NORTHWEST)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.NORTHWEST);
						this.map.get(y).set(x, new Empty());
						this.map.get(y + 1).set(x - 1, this.mower);
						this.y = y + 1;
						this.x = x - 1;
					} else if (this.mower.getDirection().equals(DIRECTION.SOUTHEAST)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.SOUTHEAST);
						this.map.get(y).set(x, new Empty());
						this.map.get(y - 1).set(x + 1, this.mower);
						this.y = y - 1;
						this.x = x + 1;
					} else if (this.mower.getDirection().equals(DIRECTION.SOUTHWEST)) {
						result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.SOUTHWEST);
						this.map.get(y).set(x, new Empty());
						this.map.get(y - 1).set(x - 1, this.mower);
						this.y = y - 1;
						this.x = x - 1;
					}

					randomSteer = false;
					alreadyScanned = false;
					// scanOnce = true;
				}
				// choose random direction that does not have an ObSticle.

			} else if (getFutureDirection().equals(this.mower.getDirection())) {
				// move
				if (this.mower.getDirection().equals(DIRECTION.EAST)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.EAST);
					this.map.get(y).set(x, new Empty());
					this.map.get(y).set(x + 1, this.mower);
					this.x = x + 1;
				} else if (this.mower.getDirection().equals(DIRECTION.WEST)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.WEST);
					this.map.get(y).set(x, new Empty());
					this.map.get(y).set(x - 1, this.mower);
					this.x = x - 1;
				} else if (this.mower.getDirection().equals(DIRECTION.NORTH)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.NORTH);
					this.map.get(y).set(x, new Empty());
					this.map.get(y + 1).set(x, this.mower);
					this.y = y + 1;
				} else if (this.mower.getDirection().equals(DIRECTION.SOUTH)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.SOUTH);
					this.map.get(y).set(x, new Empty());
					this.map.get(y - 1).set(x, this.mower);
					this.y = y - 1;
				} else if (this.mower.getDirection().equals(DIRECTION.NORTHEAST)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.NORTHEAST);
					this.map.get(y).set(x, new Empty());
					this.map.get(y + 1).set(x + 1, this.mower);
					this.y = y + 1;
					this.x = x + 1;
				} else if (this.mower.getDirection().equals(DIRECTION.NORTHWEST)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.NORTHWEST);
					this.map.get(y).set(x, new Empty());
					this.map.get(y + 1).set(x - 1, this.mower);
					this.y = y + 1;
					this.x = x - 1;
				} else if (this.mower.getDirection().equals(DIRECTION.SOUTHEAST)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.SOUTHEAST);
					this.map.get(y).set(x, new Empty());
					this.map.get(y - 1).set(x + 1, this.mower);
					this.y = y - 1;
					this.x = x + 1;
				} else if (this.mower.getDirection().equals(DIRECTION.SOUTHWEST)) {
					result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.MOVE, DIRECTION.SOUTHWEST);
					this.map.get(y).set(x, new Empty());
					this.map.get(y - 1).set(x - 1, this.mower);
					this.y = y - 1;
					this.x = x - 1;
				}
				randomSteer = false;
				alreadyScanned = false;
			} else {
				// steer to grass.
				result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(ACTION.STEER, getFutureDirection());
				scanOnce = true;
			}

		}

		return result;
	}

	private DIRECTION getFutureDirection() {

		if (y + 1 < this.map.size() && x < this.map.get(y + 1).size() && !this.map.get(y + 1).get(x).causesCrash()) {
			for (int i = y; i < this.map.size(); i++) {
				if (x < this.map.get(i).size() && this.map.get(i).get(x) instanceof Grass) {
					return DIRECTION.NORTH;
				}
			}
		}
		if (y - 1 >= 0 && x < this.map.get(y - 1).size() && !this.map.get(y - 1).get(x).causesCrash()) {
			for (int i = 0; i <= y; i++) {
				if (x < this.map.get(i).size() && this.map.get(i).get(x) instanceof Grass) {
					return DIRECTION.SOUTH;
				}
			}
		}

		// East
		if (x + 1 < this.map.get(y).size() && !this.map.get(y).get(x + 1).causesCrash()) {
			for (int i = x; i < this.map.get(y).size(); i++) {
				if (i < this.map.get(y).size() && this.map.get(y).get(i) instanceof Grass) {
					return DIRECTION.EAST;
				}
			}
		}
		if (x - 1 > 0 && x - 1 < this.map.get(y).size() && !this.map.get(y).get(x - 1).causesCrash()) {
			// West
			for (int i = 0; i <= x; i++) {
				if (this.map.get(y).get(i) instanceof Grass) {
					return DIRECTION.WEST;
				}
			}
		}
		// NorthEast
		// if (x + 1 < this.map.get(y).size() && y + 1 < this.map.size() && x <
		// this.map.get(y + 1).size()
		// && !this.map.get(y + 1).get(x + 1).causesCrash()) {
		// int Y = y + 1;
		// int X = x + 1;
		// while (Y < this.map.size() && X < this.map.get(y).size()) {
		// if (this.map.get(Y++).get(X++) instanceof Grass) {
		// return DIRECTION.NORTHEAST;
		// }
		// }
		// }

		return null;
	}

	public void doScan() {
		// we are using singleton in order to eliminate static calls and static
		// members.
		SystemMapObject[] results = scan();
		// add to its current map.
		// System.out.println(toStringInternalMap());
		updateMowerCords();
		// NorthWest
		if (y + 1 >= this.map.size()) {
			this.map.add(new ArrayList<SystemMapObject>());
			this.map.get(y + 1).add(0, results[7]);
		} else if (x - 1 <= 0) {
			this.map.get(y + 1).add(x, results[7]);
		} else {
			this.map.get(y + 1).set(x - 1, results[7]);
		}
		updateMowerCords();
		// West
		if (this.map.get(y).size() < 2) {
			this.map.get(y).add(0, results[6]);
		} else if (x - 1 < 0) {
			this.map.get(y).add(0, results[6]);
		} else {
			this.map.get(y).set(x - 1, results[6]);
		}
		// SouthWest
		updateMowerCords();
		if (y - 1 < 0) {
			this.map.add(0, new ArrayList<SystemMapObject>());
			this.map.get(0).add(0, results[5]);
		} else if (this.map.get(y - 1).size() < 2) {
			this.map.get(y - 1).add(0, results[5]);
		} else if (this.map.get(y - 1).size() <= x - 1) {
			this.map.get(y - 1).add(results[5]);
		} else {
			this.map.get(y - 1).set(x - 1, results[5]);
		}
		updateMowerCords();
		// North
		if (y + 1 < this.map.size() && x >= this.map.get(y + 1).size()) {
			if (x >= this.map.get(y + 1).size()) {
				this.map.get(y+1).add(results[0]);
			} else {
				this.map.get(y + 1).add(x, results[0]);
			}
		} else {
			this.map.get(y + 1).set(x, results[0]);
		}
		updateMowerCords();
		// NorthEast
		if (this.map.get(y + 1).size() <= x + 1) {
			this.map.get(y + 1).add(results[1]);
		} else {
			this.map.get(y + 1).set(x + 1, results[1]);
		}
		updateMowerCords();
		// East
		if (this.map.get(y).size() <= x + 1) {
			this.map.get(y).add(results[2]);
		} else {
			this.map.get(y).set(x + 1, results[2]);
		}
		updateMowerCords();

		// South
		if (y - 1 < 0) {
			this.map.add(0, new ArrayList<SystemMapObject>());
			this.map.get(0).add(results[4]);
		} else if (x >= this.map.get(y - 1).size()) {
			this.map.get(y - 1).add(results[4]);
		} else {
			this.map.get(y - 1).set(x, results[4]);
		}
		updateMowerCords();
		// South East
		if (this.map.get(y - 1).size() <= x + 1) {
			this.map.get(y - 1).add(results[3]);
		} else {
			this.map.get(y - 1).set(x + 1, results[3]);
		}

		updateMowerCords();
		// System.out.println("This is your X:" + this.x);
		// System.out.println("This is your Y:" + this.y);
		//
		// System.out.println(toStringInternalMap());
		// System.out.println("-------------------------------------------------------------------------------------");

	}

	private void updateMowerCords() {
		/// Update local map with mowers cords.
		for (int i = 0; i < this.map.size(); i++) {
			for (int j = 0; j < this.map.get(i).size(); j++) {
				if (this.map.get(i).get(j) == this.mower) {
					this.x = j;
					this.y = i;
					break;
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private String toStringInternalMap() {

		StringBuilder sb = new StringBuilder("Internal Map Mower ");
		sb.append(this.mower.getId());
		sb.append(System.lineSeparator());
		for (int i = this.map.size() - 1; i >= 0; i--) {
			for (int j = 0; j < this.map.get(i).size(); j++) {
				if (this.map.get(i).get(j) == null || this.map.get(i).get(j) instanceof Empty) {
					sb.append("empty ");
				} else if (this.map.get(i).get(j) instanceof Mower) {
					sb.append("mower ");
				} else if (this.map.get(i).get(j) instanceof Grass) {
					sb.append("grass ");
				} else if (this.map.get(i).get(j) instanceof Crater) {
					sb.append("crater");
				} else if (this.map.get(i).get(j) instanceof Fence) {
					sb.append("fence ");
				}
				sb.append(" | ");
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();

	}

}
