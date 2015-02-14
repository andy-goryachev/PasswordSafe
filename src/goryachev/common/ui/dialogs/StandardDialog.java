// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs;
import goryachev.common.ui.CDialog;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;


public class StandardDialog
	extends CDialog
{
	public final StandardDialogPanel panel;
	
	
	public StandardDialog(Component parent)
	{
		super(parent, "StandardDialog", true);
		
		panel = new StandardDialogPanel();

		setContent(panel);
		getContentPanel().setBorder(null);
	}
	
	
	public void setTextError(Throwable e)
	{
		panel.setTextError(e);
	}
	
	
	public void setTextPlain(String s)
	{
		panel.setTextPlain(s);
	}
	
	
	public void setTextHtml(String s)
	{
		panel.setTextHtml(s);
	}
	
	
	public void setLogo(Icon ic)
	{
		panel.setIcon(ic);
	}
	
	
	public void addButton(JButton b)
	{
		panel.addButton(b);
	}
	
	
	public void setButtonHighlight()
	{
		panel.setButtonHighlight();
	}


	public void setDefaultButton()
	{
		JButton b = panel.buttons().getLastButton();
		if(b != null)
		{
			setDefaultButton(b);
			setDefaultFocusComponent(b);
		}
	}
}
