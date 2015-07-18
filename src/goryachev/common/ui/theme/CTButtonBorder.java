// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;


public class CTButtonBorder
	extends CButtonBorder
{
	public CTButtonBorder()
	{
		super(false, true, false, true);
	}
	
	
	protected Color getLeftLineColor()
	{
		return Theme.brighter(Theme.panelBG());
	}
	
	
	protected Color getRightLineColor()
	{
		return Theme.darker(Theme.lineColor());
	}
}