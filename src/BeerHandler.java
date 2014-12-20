/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Beer handler class.
 */

import java.util.HashMap;
import java.util.Map;


public class BeerHandler {
	private static Map<Integer, Integer> tempTally = new HashMap<Integer, Integer>();
	
	/**
	 * Adds a tally to the roommate with the corresponding room number.
	 * @param roomNumber
	 */
	public static void addTally(int roomNumber) {
		if (tempTally.containsKey(roomNumber)) {
			tempTally.put(roomNumber, tempTally.get(roomNumber) + 1);
		}
		// if tally = 0
		else {
			tempTally.put(roomNumber, 1);
		}
	}
	
	/**
	 * Removes a tally from the roommate with the corresponding room number.
	 * Pre: Has a total tally count higher than 0.
	 * @param roomNumber
	 */
	public static void removeTally(int roomNumber) {
		if (tempTally.containsKey(roomNumber)) {
			int tally = tempTally.get(roomNumber);
			if (tally > 0) {
				tempTally.put(roomNumber, tally - 1);
			}
		}
	}
	
	/**
	 * Resets the temporary tally count.
	 */
	public static void resetTallies() {
		tempTally.clear();
	}
	
	/**
	 * Applies the temporary tally count to the database, adding up to the total count.
	 */
	public static void applyTallies() {
		DBHandler.addTallies(tempTally);
		resetTallies();
	}
	
	/**
	 * Gets the current total tallies.
	 * @return The total tallies.
	 */
	public static Map<Integer, Integer> getCurrentTallies() {
		return DBHandler.getAllTallies(DBHandler.getLatestListId());
	}

	/**
	 * Gets the uncommitted tallies.
	 * @return tempTally	The uncommited tallies.
	 */
	public static Map<Integer, Integer> getTempTally() {
		return tempTally;
	}
	
	public static void newList() {
		DBHandler.newList();
		resetTallies();
	}
	
}
