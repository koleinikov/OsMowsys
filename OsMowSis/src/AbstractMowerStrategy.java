

import java.util.Map.Entry;
/**
 * 
 * @author koleinikov3
 *
 */
public abstract class AbstractMowerStrategy implements MowerStrategy {
	protected Mower mower;
	
	public AbstractMowerStrategy(Mower mover) {
		this.mower = mover;
	}

	@Override
	public abstract Entry<ACTION, DIRECTION> decide();
	
	public SystemMapObject[] scan() {		
		return OsMowSystem.getInstance().scan(mower);	
	}

}
