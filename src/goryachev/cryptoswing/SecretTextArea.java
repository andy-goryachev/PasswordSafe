// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import javax.swing.JTextArea;


/** 
 * A text area which uses SecretDocument for storage and 
 * which does not use Document.getString() actions which leak information via String instances.
 */ 
public class SecretTextArea
	extends JTextArea
{
	public SecretTextArea()
	{
		super(new SecretDocument());
	}
}
