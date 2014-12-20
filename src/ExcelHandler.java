/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Writes the reports to an excel file.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ExcelHandler {
	/**
	 * Generate excel report of the latest list.
	 * @return The file name.
	 */
	public static String writeToExcelFile() {
		return writeToExcelFile(DBHandler.getLatestListId());
	}
	
	/**
	 * Generate the excel report of the given list id.
	 * @param listId	The id of the list to generate an excel file of.
	 * @return result	The file name.
	 */
	public static String writeToExcelFile(int listId) {
		String date = DBHandler.getListDate(listId);
		date = date.replace("-", "");
		FileOutputStream fileOut = null;
		Map<Integer, Integer> tallies = DBHandler.getAllTallies(listId);
		Map<Integer, String> roommates = DBHandler.getAllRoommates();
		String fileName = date + "-" + listId + ".xls";
		
		Workbook wb = new HSSFWorkbook();
		Sheet shTallies = wb.createSheet("Tallies");
		File file = new File("data/generated/sheets/" + fileName);
		try {
			int index = 0;
			
			for (Entry<Integer, Integer> entry : tallies.entrySet()) {
				Row row = shTallies.createRow(index);
				int roomNumber = entry.getKey();
				int tallyCount = entry.getValue();
				
				row.createCell(0).setCellValue(roommates.get(roomNumber));
				row.createCell(1).setCellValue(tallyCount);
				index++;
			}
			
			file.createNewFile();
			fileOut = new FileOutputStream(file);
			
			wb.write(fileOut);
			
			fileOut.close();		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileName;
	}
}
