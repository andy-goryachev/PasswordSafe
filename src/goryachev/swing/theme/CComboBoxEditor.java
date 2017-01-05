// Copyright © 2009-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;


public class CComboBoxEditor
	extends BasicComboBoxEditor
{
	protected JTextField createEditorComponent()
	{
		JTextField editor = super.createEditorComponent();
		editor.setBorder(AgComboBoxUI.BORDER_EDITOR);
		editor.setOpaque(false);
		return editor;
	}


	public void setItem(Object item)
	{
		super.setItem(item);
		if(editor.hasFocus())
		{
			editor.selectAll();
		}
	}
}