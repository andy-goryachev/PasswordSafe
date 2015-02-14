// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;


public class CBevelBorder
	implements Border
{
	private float factor;
	private boolean inverted;
	
	
	public CBevelBorder(float factor, boolean inverted)
	{
		this.factor = factor;
		this.inverted = inverted;
	}


	public Insets getBorderInsets(Component c)
	{
//		if(inverted)
//		{
//			return new Insets(1,1,2,2);
//		}
//		else
		{
			return new Insets(2,2,1,1);
		}
	}


	public boolean isBorderOpaque()
	{
		return true;
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		Color dark = ColorTools.darker(Theme.panelBG(), factor);
		Color bright = ColorTools.brighter(Theme.panelBG(), factor);
		
		g.setColor(dark);
		g.drawRect(x,y,width-1,height-1);
		
		g.setColor(inverted ? ColorTools.darker(dark, factor) : bright);
		
		g.drawLine(x+1, y + height - 2, x+1, y+1);
		g.drawLine(x+1, y+1, x + width - 2, y+1);
		
		// 02
//		g.setColor(topLeft);
//		g.drawLine(x+1, y + height - 2, x+1, y+1);
//		g.drawLine(x+1, y+1, x + width - 2, y+1);
//		
//		g.setColor(bottomRight);
//		g.drawLine(x + width - 2, y+1, x + width - 2, y + height - 2);
//		g.drawLine(x + width - 2, y + height - 2, x+1, y + height - 2);
		
		// 01
//		g.setColor(topLeft);
//		g.drawLine(x, y + height - 1, x, y);
//		g.drawLine(x, y, x + width - 1, y);
//		
//		g.setColor(bottomRight);
//		g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
//		g.drawLine(x + width - 1, y + height - 1, x, y + height - 1);
	}
	
	
//	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
//	{
//		Color dark = Colors.darker(Theme.panelBG(), factor);
//		Color bright = Colors.brighter(Theme.panelBG(), factor);
//		
//		g.setColor(dark);
//		g.drawRect(x,y,width-1,height-1);
//		
//		g.setColor(bright);
//		
//		if(inverted)
//		{
//			g.drawLine(x + width - 2, y+1, x + width - 2, y + height - 2);
//			g.drawLine(x + width - 2, y + height - 2, x+1, y + height - 2);
//		}
//		else
//		{
//			g.drawLine(x+1, y + height - 2, x+1, y+1);
//			g.drawLine(x+1, y+1, x + width - 2, y+1);
//		}
		
		// 02
//		g.setColor(topLeft);
//		g.drawLine(x+1, y + height - 2, x+1, y+1);
//		g.drawLine(x+1, y+1, x + width - 2, y+1);
//		
//		g.setColor(bottomRight);
//		g.drawLine(x + width - 2, y+1, x + width - 2, y + height - 2);
//		g.drawLine(x + width - 2, y + height - 2, x+1, y + height - 2);
		
		// 01
//		g.setColor(topLeft);
//		g.drawLine(x, y + height - 1, x, y);
//		g.drawLine(x, y, x + width - 1, y);
//		
//		g.setColor(bottomRight);
//		g.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
//		g.drawLine(x + width - 1, y + height - 1, x, y + height - 1);
//	}
}
