

import java.util.Map;

/**
 * @author koleinikov3
 *
 */
public interface MowerStrategy {
	//determines action, direction.
	Map.Entry<ACTION,DIRECTION> decide();
}
