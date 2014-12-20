/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Parses the quotes from text file.
 */


import java.io.*;
import java.util.ArrayList;

public class QuoteParser {
	
	public static ArrayList<String> getQuotes() {
		BufferedReader reader = null;
		String line;
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			reader = new BufferedReader(new FileReader("data/quotes.txt"));
			
			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
		
			reader.close();
					
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}

