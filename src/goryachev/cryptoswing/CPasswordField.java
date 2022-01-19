// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import goryachev.common.log.Log;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import goryachev.swing.Theme;
import javax.swing.JPasswordField;
import javax.swing.UIManager;


public final class CPasswordField
	extends JPasswordField
{
	protected static final Log log = Log.get("CPasswordField");
	
	
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
			log.error(e);
			UIManager.getLookAndFeel().provideErrorFeedback(this);
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
