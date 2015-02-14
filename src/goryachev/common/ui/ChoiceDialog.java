// Copyright (c) 2014-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.icons.CIcons;
import java.awt.Color;
import java.awt.Component;


public class ChoiceDialog
	extends CDialog
{
	public final BasePanel panel;
	private int choice = -1;
	
	
	public ChoiceDialog(Component parent, String title, String text)
	{
		super(parent, "ChoiceDialog", true);
		setTitle(title);
		getContentPanel().setBorder(null);
		setMinimumSize(450, 250);
		
		panel = new BasePanel();
		panel.setBackground(Theme.fieldBG());
		panel.setCenterText(text);
		panel.setIcon(CIcons.Question96);
		setCenter(panel);		
	}
	
	
	protected CAction createAction(final int choice)
	{
		return new CAction()
		{
			public void action()
			{
				setChoice(choice);
			}
		};
	}
	
	
	protected void setChoice(int choice)
	{
		this.choice = choice;
		close();
	}
	
	
	protected CButtonPanel createButtonPanel()
	{
		CButtonPanel p = new CButtonPanel();
		p.setBorder(new CBorder(MARGIN));
		return p;
	}
	

	public void addButton(String text, int choice)
	{
		getButtonPanel().addButton(new CButton(text, createAction(choice)));
	}
	
	
	public void addButton(String text, int choice, Color buttonColor)
	{
		getButtonPanel().addButton(new CButton(text, createAction(choice), buttonColor));
	}
	
	
	public void addButton(String text, int choice, boolean highlight)
	{
		getButtonPanel().addButton(new CButton(text, createAction(choice), highlight));
	}
	
	
	/** returns choice set by one of the buttons, or -1 if dialog was closed by the user (meaning "Cancel" */
	public int getChoice()
	{
		return choice;
	}
	
	
	public int openChoiceDialog()
	{
		open();
		return getChoice();
	}
}
