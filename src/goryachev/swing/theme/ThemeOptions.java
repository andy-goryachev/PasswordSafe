// Copyright © 2009-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;
import goryachev.swing.options.BooleanOption;
import goryachev.swing.options.DateFormatOption;
import goryachev.swing.options.NumberFormatOption;
import goryachev.swing.options.TimeFormatOption;


public class ThemeOptions
{
	// effects
	public static final BooleanOption hoverEffects = new BooleanOption("ui.theme.hover.effects", false);
	
	// formats
	public static final DateFormatOption dateFormat = new DateFormatOption("app.DateFormat");
	public static final TimeFormatOption timeFormat = new TimeFormatOption("app.TimeFormat");
	public static final NumberFormatOption numberFormat = new NumberFormatOption("app.NumberFormat");
}
