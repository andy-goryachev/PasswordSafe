// Copyright © 2009-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;
import goryachev.swing.CButton;
import goryachev.swing.CSkin;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;


// TODO kill - move paint method into gradient painter
public class CButtonSkin
	extends CSkin
{
	public void paint(Graphics g, JComponent c)
	{
		Color center = getBackground(c);
		Color top = Theme.brighter(center);
		Color bottom = Theme.darker(center);

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
					return UI.mix(c, 0.5f, x.getBackground());
				}
			}
		}
		
		return x.getBackground();
	}
}
