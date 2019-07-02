// Copyright © 2006-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.swing.theme.ALinearPanel;


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
