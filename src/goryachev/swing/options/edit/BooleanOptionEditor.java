// Copyright © 2008-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.swing.CCheckBox;
import goryachev.swing.options.BooleanOption;
import goryachev.swing.options.OptionEditor;
import javax.swing.JComponent;


public class BooleanOptionEditor
	extends OptionEditor<Boolean>
{
	public final CCheckBox editor;


	public BooleanOptionEditor(BooleanOption op, String text)
	{
		super(op);
		
		editor = new CCheckBox(text);
		editor.setOpaque(false);
	}


	public JComponent getComponent()
	{
		return editor;
	}


	public Boolean getEditorValue()
	{
		return editor.isSelected();
	}


	public void setEditorValue(Boolean on)
	{
		editor.setSelected(Boolean.TRUE.equals(on));
	}


	public String getSearchString()
	{
		return editor.getText();
	}
}