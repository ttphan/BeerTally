import java.io.*;
import java.util.ArrayList;

public class quoteParser {
	
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

