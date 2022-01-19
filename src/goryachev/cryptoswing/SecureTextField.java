// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import goryachev.common.log.Log;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import javax.swing.JTextField;
import javax.swing.text.Document;


public class SecureTextField
	extends JTextField
{
	protected static final Log log = Log.get("SecureTextField");
	
	
	public SecureTextField()
	{
		super(new SecretDocument(), null, 0);
	}
	
	
	public void setDocument(Document d)
	{
		// ignore all others
		// this happes on JTextComponent.installUI when
		// UI prematurely sets the default document
		if(d instanceof SecretDocument)
		{
			super.setDocument(d);
		}
	}


	public SecretDocument getSecretDocument()
	{
		return (SecretDocument)super.getDocument();
	}


	public OpaqueChars getOpaquePassword()
	{
		return getSecretDocument().getOpaqueChars();
	}


	public void setText(OpaqueChars pw)
	{
		char[] cs = null;
		try
		{
			cs = pw.getChars();
			getSecretDocument().replace(cs);
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			Crypto.zero(cs);
		}
	}
}
