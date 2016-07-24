// Copyright © 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.swing.theme;
import goryachev.common.swing.CSkin;
import goryachev.common.swing.Theme;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;


public class CGradientSkin
	extends CSkin
{
	private boolean vertical;
	private int topOrLeft;
	private int bottomOrRight;


	public CGradientSkin(boolean vertical, int topOrLeft, int bottomOrRight)
	{
		this.vertical = vertical;
		this.topOrLeft = topOrLeft;
		this.bottomOrRight = bottomOrRight;
	}


	public void paint(Graphics g, JComponent c)
	{
		Color centerColor = c.getBackground();
		Color topColor = Theme.brighter(centerColor);
		Color bottomColor = Theme.darker(centerColor);

		if(vertical)
		{
			GradientPainter.paintVertical(g, 0, 0, c.getWidth(), c.getHeight(), centerColor, topOrLeft, topColor, bottomOrRight, bottomColor);
		}
		else
		{
			GradientPainter.paintHorizontal(g, 0, 0, c.getWidth(), c.getHeight(), centerColor, topOrLeft, topColor, bottomOrRight, bottomColor);
		}
	}
}
