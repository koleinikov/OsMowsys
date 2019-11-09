
/**
 * 
 * @author koleinikov3
 *
 */
public class Grass extends SystemMapObject {

	/* (non-Javadoc)
	 * @see SystemMapObjects#causesCrash()
	 */
	@Override
	public boolean causesCrash() {
	
		return false;
	}
	
    @Override
    public String toString() { 
        return "grass"; 
    } 

}
