// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import javax.swing.JTextField;


/** 
 * A text field which uses SecretDocument for storage and 
 * which does not use Document.getString() actions which leak information via String instances.
 */ 
public class SecretTextField
	extends JTextField
{
	public SecretTextField()
	{
		this(0);
	}
	
	
	public SecretTextField(int columns)
	{
		super(new SecretDocument(), null, columns);
	}
}
