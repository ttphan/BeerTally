/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Roommate handler class.
 */

import java.util.Map;


public class RoommateHandler {
	
	/**
	 * The roommate names by room number.
	 */
	private static Map<Integer, String> names;
	
	/**
	 * Roommate id's by room number.
	 */
	private static Map<Integer, Integer> roommateIds;
	
	public static Map<Integer, String> getNames() {
		return names;
	}
	
	public static Map<Integer, Integer> getRoommateIds() {
		return roommateIds;
	}
	
	/**
	 * Adds a temporary tally group.
	 * @param name			Name of the group
	 * @return roomNumber	The 'room number' of the temporary tally group (19, 20 or 21)
	 */
	public static int addTempTally(String name) {
		int roomNumber = -1;
		
		if (!names.containsKey(19)) {
			roomNumber = 19;
		}
		else if (!names.containsKey(20)) {
			roomNumber = 20;
		}
		else if (!names.containsKey(21)) {
			roomNumber = 21;
		}
		else {
			throw new IndexOutOfBoundsException("No free lists for temporary tallies!");
		}
	
		DBHandler.addTempTally(name, roomNumber);
		
		return roomNumber;
	}
	
	/**
	 * Removes a temporary tally group.
	 * @param roomNumber	The 'room number' of the temporary tally group (19, 20 or 21)
	 */
	public static void removeTempTally(int roomNumber) {
		if (roomNumber > 18) {
			DBHandler.removeTempTally(roomNumber);
		}
	}
	
	/**
	 * Refreshes the names and roommate id maps.
	 */
	public static void refresh() {
		names = DBHandler.getActiveRoommates();
		roommateIds = DBHandler.getRoommateIds();
	}
}
