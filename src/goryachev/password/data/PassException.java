// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.common.i18n.TXT;


public final class PassException
	extends Exception
{
	public static final String CORRUPTED = "CORRUPTED";
	public static final String FAILED_TO_SAVE_NEW_FILE = "FAILED_TO_SAVE_NEW_FILE";
	public static final String STRING_TOO_LONG = "STRING_TOO_LONG";
	public static final String WRONG_PASSWORD = "WRONG_PASSWORD";
	public static final String WRONG_SIGNATURE = "WRONG_SIGNATURE";
	
	
	public PassException(String msg)
	{
		super(msg);
	}
	
	
	public PassException(String msg, Throwable e)
	{
		super(msg, e);
	}
	
	
	public String getLocalizedMessage()
	{
		String m = getMessage();
		if(m == CORRUPTED) return TXT.get("PassException.corrupted", "File might have been corrupted or altered.");
		else if(m == FAILED_TO_SAVE_NEW_FILE) return TXT.get("PassException.failed to save", "Unable to save database file.");
		else if(m == STRING_TOO_LONG) return TXT.get("PassException.too long", "Text exceeds maximum length.");
		else if(m == WRONG_PASSWORD) return TXT.get("PassException.wrong password", "Unable to decrypt the file with supplied password.");
		else if(m == WRONG_SIGNATURE) return TXT.get("PassException.wrong signature", "Wrong file format.");
		return m;
	}
}
