import java.util.Map;


public class RoommateHandler {
	
	private static Map<Integer, String> names;
	private static Map<Integer, Integer> roommateIds;
	
	public static Map<Integer, String> getNames() {
		return names;
	}
	
	public static Map<Integer, Integer> getRoommateIds() {
		return roommateIds;
	}
	
	public static int addNewTempTally(String name) {
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
	
		DBHandler.addNewTempTally(name, roomNumber);
		
		return roomNumber;
	}
	
	public static void refresh() {
		names = DBHandler.getActiveRoommates();
		roommateIds = DBHandler.getRoommateIds();
	}
}
