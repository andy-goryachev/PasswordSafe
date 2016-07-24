// Copyright © 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.swing.theme;
import goryachev.common.swing.options.BooleanOption;
import goryachev.common.swing.options.DateFormatOption;
import goryachev.common.swing.options.NumberFormatOption;
import goryachev.common.swing.options.TimeFormatOption;


public class ThemeOptions
{
	// effects
	public static final BooleanOption hoverEffects = new BooleanOption("ui.theme.hover.effects", false);
	
	// formats
	public static final DateFormatOption dateFormat = new DateFormatOption("app.DateFormat");
	public static final TimeFormatOption timeFormat = new TimeFormatOption("app.TimeFormat");
	public static final NumberFormatOption numberFormat = new NumberFormatOption("app.NumberFormat");
}
