/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Database handler class, responsible for the communication between the application and the
 * database.
 */

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class DBHandler {
	
	/**
	 * Location of database file.
	 */
	private static final String DB_LOCATION = "data/database.db";
  
	/**
	 * Establish DB connection.
	 * @return c	Connection object.
	 */
	private static Connection getConnection() {
		Connection c = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + DB_LOCATION);
			c.setAutoCommit(false);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		return c;
	}
	
	/**
	 * Gets the latest list id.
	 * @return result	Latest list id
	 */
	public static int getLatestListId() {
		Connection c = getConnection();
		Statement stmt = null;
		int result = -1;
		
		try {
			stmt = c.createStatement();
			String sql = "SELECT MAX(rowid) AS id FROM List;";
			ResultSet rs = stmt.executeQuery(sql); 
			
			if (rs.next()) {
				result = rs.getInt("id");				
			}
			
			
			stmt.close();
			c.close();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Gets the start date of the list.
	 * @param listId	The id of the list
	 * @return result	The start date
	 */
	public static String getListDate(int listId) {
		Connection c = getConnection();
		Statement stmt = null;
		String result = null;
		
		try {
			stmt = c.createStatement();
			String sql = "SELECT startDate FROM List WHERE rowid == " + listId + ";";
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				result = rs.getString("startDate");
			}
			
			
			stmt.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Get roommate id by roomNumber.
	 * @param roomNumber	Room number
	 * @return result		Roommate id
	 */
	public static int getRoommateId(int roomNumber) {
		Connection c = getConnection();
		Statement stmt = null;
		int result = -1;
		
		try {
			stmt = c.createStatement();
			String sql = "SELECT rowId FROM Roommate ";
			sql += 			"WHERE active == 1 AND roomNumber == " + roomNumber + ";";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				result = rs.getInt("rowid");
			}
			
			stmt.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Gets all current active roommates.
	 * @return ArrayList<Roommate> roommates	An array list with roommates
	 */
	public static Map<Integer, String> getActiveRoommates() {
		Connection c = getConnection();
		Statement stmt = null;
		Map<Integer, String> result = new HashMap<Integer, String>();
		
		try {
			stmt = c.createStatement();
			String sql = "SELECT name, roomNumber FROM Roommate WHERE active == 1;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {				
				result.put(rs.getInt("roomNumber"), rs.getString("name"));
			}
			
			
			stmt.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Get all roommates who were in the system
	 * @return result	All roommates
	 */
	public static Map<Integer, String> getAllRoommates() {
		Connection c = getConnection();
		Statement stmt = null;
		Map<Integer, String> result = new TreeMap<Integer, String>();
		
		try {
			stmt = c.createStatement();
			String sql = "SELECT name, rowid FROM Roommate;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {				
				result.put(rs.getInt("rowid"), rs.getString("name"));
			}
			
			
			stmt.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * Adds a new roommate to a specific room, replacing the old roommate.
	 * @param name			The name of the new roommate
	 * @param roomNumber	The room number of the new roommate
	 */
	public static void changeRoommate(String name, int roomNumber) {
		Connection c = getConnection();
		Statement stmt = null;
		
		try {
			stmt = c.createStatement();
			String sql = "UPDATE Roommate SET active = 0 ";
			sql += 			"WHERE active == 1 AND roomNumber == " + roomNumber + ";";
			
			stmt.executeUpdate(sql);
			
			sql = "INSERT INTO Roommate (roomNumber, name) ";
			sql += "VALUES (" + roomNumber + ", '" + name + "');";
			
			stmt.executeUpdate(sql);
			
			c.commit();
			
			stmt.close();
			c.close();		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the tally count of the roommate, based on room number.
	 * @param roomNumber	Room number of the roommate
	 * @return result		The tally count
	 */
	public static int getTally(int roomNumber) {
		Connection c = getConnection();
		Statement stmt = null;
		int result = -1;
		
		try {
			int roommateId = getRoommateId(roomNumber);
			int listId = getLatestListId();
			
			stmt = c.createStatement();
			String sql = "SELECT COUNT(" + roommateId + ") AS total FROM Tally ";
			sql += "WHERE listId == " + listId + " AND roommateId == " + roommateId + ";";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
				result = rs.getInt("total");				
			}
						
			stmt.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Gets all the tallies by list id.
	 * @param listId	The id of the list.
	 * @return result	Total tally count per roommate of the specified list
	 */
	public static Map<Integer, Integer> getAllTallies(int listId) {
		Connection c = getConnection();
		Statement stmt = null;
		Map<Integer, Integer> result = new TreeMap<Integer, Integer>();
		
		try {		
			stmt = c.createStatement();
			
			String sql = "SELECT Roommate.rowid as rowid, count(Tally.roommateId) AS total ";
			sql += "FROM Roommate LEFT JOIN Tally ON Roommate.rowid = Tally.roommateId ";
			sql += "WHERE Tally.listId = " + listId + " GROUP BY rowid;";
						
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				result.put(rs.getInt("rowid"), rs.getInt("total"));
			}
			
			stmt.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Apply tallies to database.
	 * @param tallies	Tallies to be applied
	 */
	public static void addTallies(Map<Integer, Integer> tallies) {
		Connection c = getConnection();
		Statement stmt = null;
		
		try {
			stmt = c.createStatement();	
			
			for (Entry<Integer, Integer> entry : tallies.entrySet()) {
				int roommateId = getRoommateId(entry.getKey());
				int amount = entry.getValue();
				int listId = getLatestListId();
										
				String sql = "INSERT INTO Tally (roommateId, listId) ";
				sql += "VALUES (" + roommateId + ", " + listId + ");";
				
				for (int j = 0; j < amount; j++) {
					stmt.executeUpdate(sql);
				}
			}
				
			c.commit();

			stmt.close();
			c.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the current amount of active roommates.
	 * @return result	The amount of active roommates
	 */
	public static int nrOfActiveRoommates() {
		Map<Integer, String> roommates = getActiveRoommates();
		int result = roommates.size();
		
		return result;
	}
	
	public static boolean checkPassword(String hex) {
		boolean result = false;
		Connection c = getConnection();
		Statement stmt = null;
		
		// Sanity check, to ensure that the string is a hexadecimal.
		if (hex.matches("-?[0-9a-fA-F]+")) {
			try {
				stmt = c.createStatement();
				String sql = "SELECT * FROM Password WHERE pass == '" + hex + "';";
				
				ResultSet rs = stmt.executeQuery(sql);
				
				if (rs.next()) {
					result = true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}