// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JScrollBar;


public class CScrollBarArrowButton
	extends CArrowButton
{
	private final CScrollBarUI scrollBarUI;


	public CScrollBarArrowButton(CScrollBarUI scrollBarUI, int direction)
	{
		super(direction);
		this.scrollBarUI = scrollBarUI;
	}


	public void paint(Graphics g)
	{
		int w = getWidth();
		int h = getHeight();
		Color origColor = g.getColor();
		boolean isPressed = getModel().isPressed();
		boolean isEnabled = isEnabled();

		boolean vertical = (scrollBarUI.getScrollBar().getOrientation() == JScrollBar.VERTICAL);
		GradientPainter.paint(g, !vertical, w, h, getBackground(), 50, 50, Theme.getGradientFactor(), false, true);

		if(isPressed)
		{
			Theme.loweredBevelBorder().paintBorder(this, g, 0, 0, w, h);
		}
		else
		{
			Theme.raisedBevelBorder().paintBorder(this, g, 0, 0, w, h);
		}

		if((h >= 5) && (w >= 5))
		{
			if(isPressed)
			{
				g.translate(1, 1);
			}
	
			// arrow
			int size = Math.min((h - 2) / 3, (w - 2) / 3);
			size = Math.max(size, 2);
			paintTriangle(g, (w - size) / 2, (h - size) / 2, size, direction, isEnabled);
	
			if(isPressed)
			{
				g.translate(-1, -1);
			}
		}
		g.setColor(origColor);
	}


	public Dimension getPreferredSize()
	{
		int size = 16;
		JScrollBar scrollbar = scrollBarUI.getScrollBar();
		if(scrollbar != null)
		{
			switch(scrollbar.getOrientation())
			{
			case JScrollBar.VERTICAL:
				size = scrollbar.getWidth();
				break;
			case JScrollBar.HORIZONTAL:
				size = scrollbar.getHeight();
				break;
			}
			size = Math.max(size, 5);
		}
		return new Dimension(size, size);
	}
}