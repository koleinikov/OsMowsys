
/**
 * 
 */

/**
 * @author koleinikov3
 *
 */
public class Fence extends SystemMapObject {

	/* (non-Javadoc)
	 * @see SystemMapObject#causesCrash()
	 */
	@Override
	boolean causesCrash() {
	
		return true;
	}
	
    @Override
    public String toString() { 
        return "fence"; 
    } 

}
