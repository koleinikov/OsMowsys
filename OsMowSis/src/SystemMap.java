

/**
 * This object is a map
 * 
 * @author koleinikov3
 *
 */
public class SystemMap {
	private SystemMapObject[][] map;

	private int totalNumberOfSquares;

	/**
	 * Initializes the map array to an empty map;
	 * 
	 * @param width
	 * @param height
	 */
	public SystemMap(int width, int height) {
		this.map = new SystemMapObject[height][width];
		totalNumberOfSquares = width * height;
	}

	/**
	 * Adds a mower to the map
	 * 
	 * @param x
	 * @param y
	 * @param mower
	 */
	public void addMower(int x, int y, Mower mower) {
		map[y][x] = mower;
	}
	/**
	 * Removes mower from map usually after a crash.
	 * @param x
	 * @param y
	 */
	public void removeMower(Mower mower) 
	{
		for(int i =0; i<this.map.length;i++) 
		{
			for(int j =0;j<this.map[i].length;j++) {
				if(this.map[i][j]==mower) 
				{
					this.map[i][j] = new Empty();
				}
			}
		}
	}
	/**
	 * Adds a crater to the map
	 * 
	 * @param x
	 * @param y
	 * @param crater
	 */
	public void addCrater(int x, int y, Crater crater) {
		map[y][x] = crater;
	}

	/**
	 * Fills in the empty spaces with grass
	 */
	public void fillGrass() {
		for (int i = 0; i < this.map.length; i++) {
			for (int j = 0; j < this.map[i].length; j++) {
				if (this.map[i][j] == null) {
					this.map[i][j] = new Grass();
				}
			}
		}
	}

	public SystemMapObject[] scan(Mower m) {
		SystemMapObject[] result = new SystemMapObject[8];
		for (int i = this.map.length - 1; i >= 0; i--) {
			for (int j = 0; j < this.map[i].length; j++) {
				if (this.map[i][j] instanceof Mower) {
					Mower m1 = (Mower) this.map[i][j];
					if (m1.getId() == m.getId()) {
						// build array
						// start with north and then go clockwise
						// north
						if (this.map.length <= i + 1) {
							result[0] = new Fence();
						} else {
							result[0] = this.map[i + 1][j];
						}
						// north east
						if (this.map.length <= i + 1 || this.map[i + 1].length <= j + 1) {
							result[1] = new Fence();
						} else {
							result[1] = this.map[i + 1][j + 1];
						}
						// east
						if (this.map[i].length <= j + 1) {
							result[2] = new Fence();
						} else {
							result[2] = this.map[i][j + 1];
						}
						// south east
						if (i - 1 < 0 || this.map[i - 1].length <= j + 1) {
							result[3] = new Fence();
						} else {
							result[3] = this.map[i - 1][j + 1];
						}
						// south
						if (i - 1 < 0) {
							result[4] = new Fence();
						} else {
							result[4] = this.map[i - 1][j];
						}
						// south west
						if (i - 1 < 0 || j - 1 < 0) {
							result[5] = new Fence();
						} else {
							result[5] = this.map[i - 1][j - 1];
						}
						// west
						if (j - 1 < 0) {
							result[6] = new Fence();
						} else {
							result[6] = this.map[i][j - 1];
						}
						// north west
						if (this.map.length <= i + 1 || j - 1 < 0) {
							result[7] = new Fence();
						} else {
							result[7] = this.map[i + 1][j - 1];
						}
						return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns the 2d representation of the map
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = this.map.length - 1; i >= 0; i--) {
			for (int j = 0; j < this.map[i].length; j++) {
				if (this.map[i][j] == null || this.map[i][j]instanceof Empty) {
					sb.append("empty ");
				} else if (this.map[i][j] instanceof Mower) {
					sb.append("mower ");
				} else if (this.map[i][j] instanceof Grass) {
					sb.append("grass ");
				} else if (this.map[i][j] instanceof Crater) {
					sb.append("crater");
				}
				sb.append(" | ");
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public int getTotalNumberOfSquares() {
		return totalNumberOfSquares;
	}

	public int getOriginalNumberOfGrassSquares() {
		int count = totalNumberOfSquares;
		// now let's substract craters.
		int countCraters = 0;
		
		for (int i = 0; i < this.map.length; i++) {
			for (int j = 0; j < this.map[i].length; j++) {
				SystemMapObject square = this.map[i][j];
				if (square instanceof Crater) {
					countCraters++;
				}
				
			}
		}
		count -= (countCraters );
		return count;
	}

	public int getNumberOfCutGrassSquares() {
		// the spec says that grass is cut under mover, so let's take it into
		// consideration.
		int count = 0;
		for (int i = 0; i < this.map.length; i++) {
			for (int j = 0; j < this.map[i].length; j++) {
				SystemMapObject square = this.map[i][j];
				if (square instanceof Empty || square instanceof Mower) {
					count++;
				}
			}
		}
		return count;
	}

	public boolean isMowed() {
		boolean grassMowed = true;
		for (int i = 0; i < this.map.length; i++) {
			for (int j = 0; j < this.map[i].length; j++) {
				SystemMapObject square = this.map[i][j];
				if (square instanceof Grass) {
					grassMowed = false;
					break;
				}
			}
		}
		return grassMowed;
	}

	// direction is valid, the caller should guarantee it and we do
	public void moveMower(Mower mower) {
		for (int i = 0; i < this.map.length; i++) {
			for (int j = 0; j < this.map[i].length; j++) {
				SystemMapObject square = this.map[i][j];
				if (square instanceof Mower && ((Mower) square).getId() == mower.getId()) {
					// we found the mower
					map[i][j] = new Empty();
					int heightIndex = i;
					int widthIndex = j;
					// set the square that mower is leaving to Empty
					switch (mower.getDirection()) {
					case NORTH: {
						heightIndex += 1;
						break;
					}
					case NORTHEAST:{
						heightIndex += 1;
						widthIndex+=1;
						break;
					}
					case EAST:{
						widthIndex+=1;
						break;
					}
					case SOUTHEAST:{
						heightIndex -= 1;
						widthIndex+=1;
						break;
					}
					case SOUTH: {
						heightIndex -= 1;
						break;
					}
					case SOUTHWEST:{
						heightIndex -= 1;
						widthIndex-=1;			
						break;
					}
					case WEST:{
						widthIndex-=1;
						break;
					}
					case NORTHWEST:{
						heightIndex += 1;
						widthIndex-=1;
						break;
					}
					}
					map[heightIndex][widthIndex] = square;
					return;
				}
			}
		}
	}
}
