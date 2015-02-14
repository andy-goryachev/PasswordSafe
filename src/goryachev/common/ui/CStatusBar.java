// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CPlatform;
import java.awt.Color;
import javax.swing.JLabel;


public class CStatusBar
	extends HorizontalLayoutPanel
{
	public CStatusBar()
	{
		if(CPlatform.isMac())
		{
			setBorder(new CBorder(2, 1, 2, 20));
		}
		else
		{
			setBorder(new CBorder(2, 1, 0, 1));
		}
	}


	public void copyright()
	{
		JLabel c = new JLabel(Application.getCopyright(), null, JLabel.RIGHT);
		c.setForeground(Color.gray);
		add(c);
	}
}
