// Copyright © 2008-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.swing.options.LongOption;
import goryachev.swing.options.OptionEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;


public class LongOptionEditor
	extends OptionEditor<Long>
{
	public final JTextField editor = new JTextField();


	public LongOptionEditor(LongOption op)
	{
		super(op);
	}
	

	public Long getEditorValue()
	{
		try
		{
			// TODO format
			String s = editor.getText();
			return Long.parseLong(s);
		}
		catch(Exception e)
		{
			// TODO default? beep?
			return null;
		}
	}


	public JComponent getComponent()
	{
		return editor;
	}


	public void setEditorValue(Long value)
	{
		// TODO format
		editor.setText(value == null ? null : String.valueOf(value));
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}