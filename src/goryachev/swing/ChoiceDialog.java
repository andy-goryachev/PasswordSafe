// Copyright © 2014-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.swing.icons.CIcons;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Action;
import javax.swing.text.JTextComponent;


public class ChoiceDialog<T>
	extends CDialog
{
	private T choice;
	
	
	public ChoiceDialog(Component parent, String title, String text)
	{
		super(parent, "ChoiceDialog", true);
		setTitle(title);
		Dialogs.size(this);
		borderless();
		setCloseOnEscape();
		
		panel().setLeading(Panels.iconField(CIcons.Question96));

		JTextComponent t = Panels.textComponent(text);
		t.setBorder(new CBorder(20));
		panel().setCenter(Panels.scroll(t));
	}
	
	
	protected Action createAction(final T choice)
	{
		return new XAction(() -> setChoice(choice));
	}
	
	
	protected void setChoice(T choice)
	{
		this.choice = choice;
		close();
	}
	

	public void addButton(String text, T choice)
	{
		buttonPanel().addButton(new CButton(text, createAction(choice)));
	}
	
	
	public void addButton(String text, T choice, Color buttonColor)
	{
		buttonPanel().addButton(new CButton(text, createAction(choice), buttonColor));
	}
	
	
	public void addButton(String text, T choice, boolean highlight)
	{
		buttonPanel().addButton(new CButton(text, createAction(choice), highlight));
	}
	
	
	/** returns choice set by one of the buttons, or default choice if set, or null */
	public T getChoice()
	{
		return choice;
	}
	
	
	public void setChoiceDefault(T def)
	{
		choice = def;
	}
	
	
	public T openChoiceDialog()
	{
		setDefaultButton();
		open();
		return getChoice();
	}
}
