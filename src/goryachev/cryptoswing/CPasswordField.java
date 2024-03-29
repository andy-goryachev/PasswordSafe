// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import javax.swing.JPasswordField;


public final class CPasswordField
	extends JPasswordField
{
	public CPasswordField()
	{
		super(new SecretDocument(), null, 0);
		setBorder(Theme.fieldBorder());
	}
	

	public void setPassword(OpaqueChars x)
	{
		if(x == null)
		{
			clear();
		}
		else
		{
			char[] cs = null;
	
			try
			{
				cs = x.getChars();
				setPassword(cs);
			}
			finally
			{
				Crypto.zero(cs);
			}
		}
	}
	
	
	public void setPassword(char[] cs)
	{
		try
		{
			SecretDocument d = (SecretDocument)getDocument();
			d.replace(cs);
		}
		catch(Exception e)
		{
			UI.beep();
		}
	}


	public OpaqueChars getOpaquePassword()
	{
		char[] cs = null;
		try
		{
			cs = getPassword();
			OpaqueChars op = new OpaqueChars();
			op.set(cs);
			return op;
		}
		finally
		{
			Crypto.zero(cs);
		}
	}
	
	
	public void clear()
	{
		((SecretDocument)getDocument()).clear();
		setText(null);
	}
	
	
	public boolean hasText()
	{
		return (getDocument().getLength() > 0);
	}
}
