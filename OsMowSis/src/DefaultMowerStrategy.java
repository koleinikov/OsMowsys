

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author koleinikov3
 * Mower makes random moves similar to to the sample code
 */
public class DefaultMowerStrategy extends AbstractMowerStrategy {

	public DefaultMowerStrategy(Mower mower) {
		super(mower);
	}

	@Override
	public Entry<ACTION, DIRECTION> decide() {
		ACTION action;
		DIRECTION direction = mower.getDirection();
		Random randGenerator = new Random();		 
        int moveRandomChoice = randGenerator.nextInt(100);
        if (moveRandomChoice < 10) {
            // do nothing
            action = ACTION.PASS;
        } else if (moveRandomChoice < 35) {
            // check your surroundings
            action = ACTION.SCAN;
        } else if (moveRandomChoice < 60) {
            // change direction
            action = ACTION.STEER;
            direction = getDirection();
        } else {
            // move forward
            action = ACTION.MOVE;
        }
        

		Entry<ACTION, DIRECTION> result = new AbstractMap.SimpleEntry<ACTION, DIRECTION>(action, direction);
		
		return result;
	}
	
	// caller would invoke this method only for ACTION.STEER
	private DIRECTION getDirection() {
		DIRECTION direction = mower.getDirection();
		Random randGenerator = new Random();
		 int moveRandomChoice = randGenerator.nextInt(100);
		 if (moveRandomChoice < 85) {
			 DIRECTION[] directions = DIRECTION.values();
	            int ptr = 0;
	            while(!direction.equals(directions[ptr]) && ptr < directions.length) {
	                ptr++;
	            }
	            direction = directions[(ptr + 1) % directions.length];
	        } 
		 return direction;
		
	}
}
