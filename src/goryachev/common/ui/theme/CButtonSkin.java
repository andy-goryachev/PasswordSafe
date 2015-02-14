// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CSkin;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;


// TODO kill - move paint method into gradient painter
public class CButtonSkin
	extends CSkin
{
	public void paint(Graphics g, JComponent c)
	{
		//Insets m = c.getInsets();
		//int w = c.getWidth();
		//int h = c.getHeight();
		
		Color center = getBackground(c);
		Color top = Theme.brighter(center);
		Color bottom = Theme.darker(center);
		//GradientPainter.paintVertical(g, m.left, m.top, w-m.left-m.right, h-m.top-m.bottom, center, 50, top, 50, bottom);
		GradientPainter.paintVertical(g, 0, 0, c.getWidth(), c.getHeight(), center, 50, top, 50, bottom);
	}
	
	
	protected Color getBackground(JComponent x)
	{
		if(x instanceof CButton)
		{
			CButton b = (CButton)x;
			if(b.isEnabled())
			{
				Color c = b.getHighlight();
				if(c != null)
				{
					return UI.mix(92, c, x.getBackground());
				}
			}
		}
		
		return x.getBackground();
	}
}
