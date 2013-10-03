package ctech.nxtuniverse;

import android.util.Log;

public class Map {

	public Map(int x, int y) {
		objectMap = new boolean[x][y];
		positionMap = new boolean[x][y];
	}

	private boolean[][] objectMap;
	private boolean[][] positionMap;
	private int[] robotPosition = new int[2];

	private int origin = 0;
	
	// boolean[][] map = new boolean[][] {
	// { true, false, true }, { false, true, false }
	// };

	/**
	 * Set the position of the robot.
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
        if (x >= origin && y >= origin && x < positionMap.length && y < positionMap[x].length) {
            // Remove previous position
            positionMap[positionMap.length - 1 - robotPosition[1]][robotPosition[0]] = false;

            // Set position
            positionMap[positionMap.length - 1 - y][x] = true;
            robotPosition[0] = x;
            robotPosition[1] = y;
        } else {
			Log.i("Pos Setter", "(" + x + "," + y + ") - "
					+ "Invalid coordiantion. Out of map's range");
		}
	}

	/**
	 * Set an object on the map.
	 * 
	 * @param x
	 * @param y
	 */
	public void setObject(int x, int y) {
		if (x >= origin && y >= origin && x < objectMap.length && y < objectMap[x].length) {
            // Set Object
            objectMap[objectMap.length - 1 - y][x] = true;
        } else {
			Log.i("Obj Setter", "(" + x + "," + y + ") - "
					+ "Invalid coordination. Out of map's range");
		}
	}
	
	/**
	 * Remove an object from the map.
	 * @param x
	 * @param y
	 */
	public void removeObject(int x, int y){
        if (x >= origin && y >= origin && x < objectMap.length && y < objectMap[x].length) {
            // Rm Object
            objectMap[objectMap.length - 1 - y][x] = false;
        } else {
        	Log.i("Obj Rm", "(" + x + "," + y + ") - "
					+ "Invalid coordination. Out of map's range");
        }
	}
	
	public void moveToNextBlock(){
		
	}

	/**
	 * Logs the map in LogCat.
	 */
	public void printObjectMap() {
		String localString = "";
		for (int i = 0; i < objectMap.length; i++) {
			for (int j = 0; j < objectMap[i].length; j++) {
				if (objectMap[i][j]) {
					localString += "[x]";
				} else {
					localString += "[ ]";
				}
			}
			Log.i("map " + i, localString);
			localString = "";
		}
	}

	/**
	 * Logs the map in LogCat.
	 */
	public void printPositionMap() {
		String localString = "";
		for (int i = 0; i < positionMap.length; i++) {
			for (int j = 0; j < positionMap[i].length; j++) {
				if (positionMap[i][j]) {
					localString += "[O]";
				} else {
					localString += "[ ]";
				}
			}
			Log.i("map " + i, localString);
			localString = "";
		}
	}

}
