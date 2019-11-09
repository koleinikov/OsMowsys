

/**
 * 
 * @author koleinikov3
 *
 */
public class Crater extends SystemMapObject {

	public Crater() {
		
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
        return "crater"; 
    } 

}
