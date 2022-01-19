// Copyright © 2008-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.swing.options.IntOption;
import goryachev.swing.options.IntegerOption;
import goryachev.swing.options.OptionEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;


public class IntegerOptionEditor
	extends OptionEditor<Integer>
{
	public final JTextField editor = new JTextField();


	public IntegerOptionEditor(IntegerOption op)
	{
		super(op);
	}
	
	
	public IntegerOptionEditor(IntOption op)
	{
		super(op);
	}


	public Integer getEditorValue()
	{
		try
		{
			// TODO format
			String s = editor.getText();
			return Integer.parseInt(s);
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


	public void setEditorValue(Integer value)
	{
		// TODO format
		editor.setText(value == null ? null : String.valueOf(value));
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}