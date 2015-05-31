// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CPanel3;
import goryachev.common.ui.Theme;
import javax.swing.JLabel;


public class WizardTitleBar
	extends CPanel3
{
	public final JLabel titleField;
	
	
	public WizardTitleBar()
	{
		titleField = new JLabel();
		titleField.setFont(Theme.titleFont());
		titleField.setForeground(Theme.fieldFG());
		titleField.setBorder(new CBorder(10));
		
		setCenter(titleField);
		setBorder(new CBorder(0, 0, 1, 0, Theme.lineColor()));
	}


	public void setText(String s)
	{
		titleField.setText(s);
	}
}
