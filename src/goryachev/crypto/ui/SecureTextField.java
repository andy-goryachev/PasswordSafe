// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.ui;
import javax.swing.JTextField;
import javax.swing.text.Document;


public class SecureTextField
	extends JTextField
{
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
}
