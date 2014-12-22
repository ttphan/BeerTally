/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Parses the quotes from text file.
 */

import java.io.*;
import java.util.ArrayList;

public class QuoteParser {
	
	private static final String QUOTE_FILE = "data/quotes.txt";
	
	public static ArrayList<String> getQuotes() {
		BufferedReader reader = null;
		String line;
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			reader = new BufferedReader(new FileReader(QUOTE_FILE));
			
			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
		
			reader.close();
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void addQuote(String name, String quote) {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(QUOTE_FILE, true));
			String line = name + ": " + quote;
			
			writer.newLine();
			writer.write(line);
			
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

