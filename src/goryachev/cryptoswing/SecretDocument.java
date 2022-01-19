// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import goryachev.crypto.OpaqueChars;
import goryachev.swing.UI;
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
			UI.beep();
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
			return new OpaqueChars();
		}
	}
}
