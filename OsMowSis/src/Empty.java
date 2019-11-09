
/**
 * @author koleinikov3
 *
 */
public class Empty extends SystemMapObject {

	/* (non-Javadoc)
	 * @see SystemMapObjects#causesCrash()
	 */
	@Override
	public boolean causesCrash() {
		
		return false;
	}
	
    @Override
    public String toString() { 
        return "empty"; 
    } 

}
