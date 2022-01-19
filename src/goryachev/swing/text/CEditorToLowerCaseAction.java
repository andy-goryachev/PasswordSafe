// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.text;
import goryachev.common.log.Log;
import goryachev.swing.UI;
import java.util.Locale;
import javax.swing.JEditorPane;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;


public class CEditorToLowerCaseAction
	extends CEditorAction
{
	protected static final Log log = Log.get("CEditorToLowerCaseAction");
	
	
	public CEditorToLowerCaseAction()
	{
		super("toLowerCaseAtion");
	}


	// this is very similar to CEditorToUpperCaseAction, could be made into a [selection or word] base class
	public void action()
	{
		JEditorPane ed = getEditor();
		if(ed != null)
		{
			try
			{
				Caret ca = ed.getCaret();
				int beg = Math.min(ca.getMark(), ca.getDot());
				int end = Math.max(ca.getMark(), ca.getDot());
				if(beg == end)
				{
					// select word
					beg = getWordStart(ed, beg);
					end = getWordEnd(ed, end);
					if(beg < 0)
					{
						return;
					}
					else if(end < 0)
					{
						return;
					}
					else if(beg == end)
					{
						return;
					}
				}
				
				DefaultStyledDocument d = getStyledDocument(ed);
				String s = d.getText(beg, end - beg);
				Locale loc = CEditorKit.getLocale(ed);
				s = s.toLowerCase(loc);
				d.replace(beg, end - beg, s, null);
			}
			catch(Exception e)
			{
				log.error(e);
				UI.beep();
			}
		}
	}
}