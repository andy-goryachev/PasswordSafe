// Copyright © 2006-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.swing.theme.ALinearPanel;


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