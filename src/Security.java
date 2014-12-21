/**
 * Copyright (C) De Gezevende Fles
 * Written by Tung Phan <tungphan91@gmail.com>, December 2014
 * 
 * Security functions.
 */

import java.security.MessageDigest;


public class Security {
	/**
	 * Checks password.
	 * @param message	The password to be encrypted and compared to
	 * @return result	True if the password matches.
	 */
	public static boolean checkPassword(char[] message) {
		boolean result = false;
				
		StringBuffer hexString = encryptMd5(message);
	
		if (DBHandler.checkPassword(hexString.toString())) {
       		result = true;
       	}
		
		return result;
	}
	
	/**
	 * Encrypts the message using MD5.
	 * @param message		The message to be encrypted
	 * @return hexString	The encrypted message
	 */
	public static StringBuffer encryptMd5(char[] message) {
		StringBuffer hexString = null;
		
		try {					
			byte[] pass = new String(message).getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(pass);
			byte[] messageDigest = md.digest();
			
			hexString = new StringBuffer();
            for (int i = 0; i<messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append('0');

                hexString.append(hex);
            }
		} catch (Exception err) {
			err.printStackTrace();
		}
		
		return hexString;	
	}
	
	/**
	 * Inserts new password into database
	 * @param message	The pre-encrypted password
	 */
	public static void newPassword(char[] message) {
		DBHandler.newPassword(encryptMd5(message).toString());
	}
	
	/**
	 * Only accept strings containing alphanumeric characters,
	 * hyphens and underscores of length 1 to 16.
	 * @param s		The string to be validated
	 * @return		True if it's valid
	 */
	public static boolean isValid(String s) {
		return s.matches("^[a-zA-Z0-9-_]{1,16}$");		
	}
}
