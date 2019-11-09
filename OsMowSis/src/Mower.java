

import java.util.Map;
/**
 * 
 * @author koleinikov3
 *
 */
public class Mower extends SystemMapObject {

	private MowerStrategy strategy;

	private DIRECTION direction;

	// @TODO mower is ok on creation, the OsMowSystem would set it to TRUE if the
	// mover crashed.
	private Boolean crashed = Boolean.FALSE;

	public Boolean getCrashed() {
		return crashed;
	}

	public void setCrashed(Boolean crashed) {
		this.crashed = crashed;
	}

	private int id;

	// @TODO we are overriding equals in order to use contains()
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		Mower mower = (Mower) obj;
		return id == mower.id;
	}

	// @TODO we are overriding hashCode in order to use contains(). if you override
	// equals you have to override hashCode.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Integer.valueOf(id) == null) ? 0 : Integer.valueOf(id).hashCode());
		return result;
	}

	/**
	 * Initiates the mower to a certain direction.
	 * 
	 * @param direction
	 */
	public Mower(String direction, int id, int strategyId) {

		this.direction = DIRECTION.valueOf(direction.toUpperCase());
		this.id = id;
		if (strategyId == 1) {
			strategy = new BestMowerStrategy(this);
		}else {
			strategy = new DefaultMowerStrategy(this);
		}
	}

	/**
	 * Decides whether the mower will do one of the four actions. move() steer()
	 * scan() pass()
	 */
	public SystemMessage decide() {
		// @TODO here we have to invoke strategy. for scan we would have to call
		// OsMowSystem.
		// it looks ugly. but it is driven by current uml approach. we have to obtain
		// scan results from the OsMowSystem.
		SystemMessage decision = new SystemMessage();
		// @TODO pick up strategy. there would be 2 strategy.
		// direction and set yp direction in the mower.
		// action: -----> goes to message
		Map.Entry<ACTION, DIRECTION> result = strategy.decide();
		direction = result.getValue();
		decision.setMessage(result.getKey().name());
		return decision;

	}

	@SuppressWarnings("unused")
	private void move() {
	}

	@SuppressWarnings("unused")
	private void steer() {
	}

	// scans the surrounding area.
	@SuppressWarnings("unused")
	private void scan() {

	}

	@SuppressWarnings("unused")
	private void stay() {
	}

	public DIRECTION getDirection() {
		return direction;
	}

	public void setDirection(DIRECTION direction) {
		this.direction = direction;
	}

	public int getId() {
		return this.id;
	}

	/**
	 * Defines whether moving into this object causes a crash.
	 */
	@Override
	public boolean causesCrash() {
		return true;
	}

	@Override
	public String toString() {
		return "mower";
	}

}
