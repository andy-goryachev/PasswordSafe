// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.i18n.TXT;


public final class PassException
	extends Exception
{
	public enum Error
	{
		CORRUPTED,
		FAILED_TO_SAVE_NEW_FILE,
		STRING_TOO_LONG,
		WRONG_PASSWORD,
		WRONG_SIGNATURE
	}
	
	//
	
	private final Error error;
	
	
	public PassException(Error err)
	{
		this.error = err;
	}
	
	
	public PassException(Error err, Throwable e)
	{
		super(e);
		this.error = err;
	}
	
	
	public String getLocalizedMessage()
	{
		switch(error)
		{
		case CORRUPTED:
			return TXT.get("PassException.corrupted", "File might have been corrupted or altered.");
		case FAILED_TO_SAVE_NEW_FILE:
			return TXT.get("PassException.failed to save", "Unable to save database file.");
		case STRING_TOO_LONG:
			return TXT.get("PassException.too long", "Text exceeds maximum length.");
		case WRONG_PASSWORD:
			return TXT.get("PassException.wrong password", "Unable to decrypt the file with supplied password.");
		case WRONG_SIGNATURE:
			return TXT.get("PassException.wrong signature", "Wrong file format.");
		default:
			return "?" + error;
		}
	}
}
