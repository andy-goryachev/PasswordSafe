// Copyright © 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.swing;
import goryachev.common.swing.theme.ALinearPanel;


/** Panel lays out components horizontally */
public class HorizontalPanel
	extends ALinearPanel
{
	public HorizontalPanel(int gap)
	{
		super(true, gap);
		setGap(gap);
	}
	
	
	public HorizontalPanel()
	{
		super(true);
	}
	
	
	public HorizontalPanel(boolean opaque)
	{
		this();
		setOpaque(opaque);
	}
}