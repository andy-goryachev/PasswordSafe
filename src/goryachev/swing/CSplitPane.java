// Copyright © 2008-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.swing.theme.AgSplitPaneUI;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JSplitPane;


public class CSplitPane
	extends JSplitPane
{
	private Color dividerColor;
	
	
	public CSplitPane(boolean horizontal, Component a, Component b)
	{
		this(horizontal ? HORIZONTAL_SPLIT : VERTICAL_SPLIT, a, b);
	}
	
	
	public CSplitPane(int orientation, Component a, Component b)
	{
		super(orientation, true, a, b);
		setBackground(Theme.TEXT_BG);
		setContinuousLayout(true);
//		setDividerLocation(100);
	}

	
	public void updateUI()
	{
		setUI(new AgSplitPaneUI());
	}
	
	
	/**
	 * Caution: this method is not applicable in cases where component orientation may be changed.
	 */
	public void setDividerLocation(int location)
	{
		super.setDividerLocation(location);
	}
	
	
	public void setSplitHorizontal()
	{
		setOrientation(HORIZONTAL_SPLIT);
	}
	
	
	public void setSplitVertical()
	{
		setOrientation(VERTICAL_SPLIT);
	}
	
	
	public Color getDividerColor()
	{
		return dividerColor;
	}
	
	
	public void setDividerColor(Color c)
	{
		dividerColor = c;
	}
}
