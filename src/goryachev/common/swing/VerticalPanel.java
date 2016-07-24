// Copyright © 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.swing;
import goryachev.common.swing.theme.ALinearPanel;


/** panel lays out components vertically */
public class VerticalPanel
	extends ALinearPanel
{
	public VerticalPanel(int gap)
	{
		super(false, gap);
		setGap(gap);
	}
	
	
	public VerticalPanel()
	{
		super(false);
	}
	
	
	public VerticalPanel(boolean opaque)
	{
		this();
		setOpaque(opaque);
	}
}
