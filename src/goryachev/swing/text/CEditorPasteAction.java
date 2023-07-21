// Copyright © 2012-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.text;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;


public class CEditorPasteAction
	extends CEditorAction
{
	public CEditorPasteAction()
	{
		super(DefaultEditorKit.pasteAction);
	}


	public void action()
	{
		JTextComponent c = getTextComponent();
		if(c != null)
		{
			c.paste();
		}
	}
}