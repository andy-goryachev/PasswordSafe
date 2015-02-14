// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.ui;
import goryachev.crypto.OpaqueChars;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


public class SecretDocument
	extends PlainDocument
{
	public SecretDocument()
	{
		super(new SecretContent());
	}
	
	
	public final void clear()
	{
		// TODO
	}
	
	
	public final void replace(char[] cs) throws Exception
	{
		if(cs == null)
		{
			cs = new char[0];
		}
		
		// FIX creates a non-opaque string
		replace(0, getLength(), new String(cs), null);
	}
	
	
	public final OpaqueChars getOpaqueChars() throws Exception
	{
		return ((SecretContent)getContent()).getOpaqueChars(0, getLength());
	}
}
