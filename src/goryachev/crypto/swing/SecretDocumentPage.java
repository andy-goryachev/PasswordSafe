// Copyright © 2009-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.swing;
import goryachev.swing.CPanel;
import goryachev.swing.CScrollPane;
import goryachev.swing.Theme;
import javax.swing.JTextArea;


public class SecretDocumentPage
	extends CPanel
{
	public SecretDocumentPage()
	{
		JTextArea t = new JTextArea(new SecretDocument());
		t.setFont(Theme.plainFont());
		CScrollPane scroll = new CScrollPane(t, CScrollPane.VERTICAL_SCROLLBAR_ALWAYS, CScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		setCenter(scroll);
	}
}
