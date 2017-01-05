// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import goryachev.common.util.Log;
import goryachev.crypto.OpaqueChars;
import javax.swing.text.PlainDocument;


/** 
 * Attempted implementation of an opaque Document. 
 * However, due to the nature of Document interface, it leaks sensitive material via String objects.
 * More work is needed, perhaps implement our own text components.
 */
public class SecretDocument
	extends PlainDocument
{
	public SecretDocument()
	{
		super(new SecretContent());
	}
	
	
	public final void clear()
	{
		replace(null);
	}
	
	
	public final void replace(char[] cs)
	{
		try
		{
			if(cs == null)
			{
				cs = new char[0];
			}
			
			// WARNING
			// leaks cleartext through a String 
			replace(0, getLength(), new String(cs), null);
		}
		catch(Exception e)
		{
			Log.ex(e);
		}
	}
	
	
	public final OpaqueChars getOpaqueChars()
	{
		try
		{
			return ((SecretContent)getContent()).getOpaqueChars(0, getLength());
		}
		catch(Exception e)
		{
			Log.ex(e);
			return new OpaqueChars();
		}
	}
}
