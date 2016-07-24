// Copyright © 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;
import goryachev.swing.Theme;
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
		return ThemeTools.BRIGHTER;
	}
	
	
	protected Color getRightLineColor()
	{
		return ThemeTools.DARKER;
	}
}
