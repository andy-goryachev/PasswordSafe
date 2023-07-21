// Copyright © 2012-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.memsafecryptoswing;
import goryachev.memsafecrypto.CCharArray;
import goryachev.memsafecrypto.OpaqueChars;
import goryachev.swing.UI;
import javax.swing.text.PlainDocument;


/** 
 * Attempted implementation of an opaque Document. 
 * However, due to the nature of Document interface, it leaks sensitive material via String objects.
 * More work is needed, perhaps implement our own text components.
 * 
 * TODO
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
	
	
	public final void replace(CCharArray cs)
	{
		try
		{
			replace(0, getLength(), "", null);

			if(cs != null)
			{
				int sz = cs.length();
				for(int i=0; i<sz; i++)
				{
					// information is leaked through a series of single character Strings
					String s = String.valueOf(cs.get(i));
					replace(i, 0, s, null);
				}
			}
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
