// Copyright © 2008-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.swing.options.COption;


public abstract class EditableChoiceOptionEditor<T>
	extends ChoiceOptionEditor<T>
{
	public EditableChoiceOptionEditor(COption<T> option)
	{
		super(option);

		combo.setEditable(true);
	}
}
