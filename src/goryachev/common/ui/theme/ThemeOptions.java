// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import goryachev.common.ui.options.BooleanOption;
import goryachev.common.ui.options.ColorOption;
import goryachev.common.ui.options.DateFormatOption;
import goryachev.common.ui.options.FontOption;
import goryachev.common.ui.options.NumberFormatOption;
import goryachev.common.ui.options.TimeFormatOption;
import java.awt.Color;
import java.awt.Font;


public class ThemeOptions
{
	// colors
	public static final ColorOption buttonHighlight = new ColorOption("ui.theme.color.button.highlight", Color.green);
	public static final ColorOption buttonHighlightAlternative = new ColorOption("ui.theme.color.button.highlight.alternative", Color.magenta);
	public static final ColorOption fieldBG = new ColorOption("ui.theme.color.field.bg", 0xf9f8f7); // ColorTools.mix(0.85f, textBG(), panelBG());
	public static final ColorOption fieldFG = new ColorOption("ui.theme.color.field.fg", 0x595959); // ColorTools.mix(0.65f, textFG(), textBG());
	public static final ColorOption focusColor = new ColorOption("ui.theme.color.focus", 90, 90, 90);
	public static final ColorOption hoverColor = new ColorOption("ui.theme.color.hover", 253, 178, 84);
	public static final ColorOption lineColor = new ColorOption("ui.theme.color.line", 0xaca8a2);
	public static final ColorOption linkColor = new ColorOption("ui.theme.color.link", 0, 102, 0);
	public static final ColorOption panelBG = new ColorOption("ui.theme.color.panel.bg", 212, 208, 200);
	public static final ColorOption selectionColor = new ColorOption("ui.theme.color.selection", Color.yellow);
	public static final ColorOption tableGrid = new ColorOption("ui.theme.color.table.grid", 242, 242, 242);
	public static final ColorOption toolbarBG = new ColorOption("ui.theme.color.toolbar.bg", 0xb2aea7);

	// effects
	public static final BooleanOption hoverEffects = new BooleanOption("ui.theme.hover.effects", false);
	
	// formats
	public static final DateFormatOption dateFormat = new DateFormatOption("app.DateFormat");
	public static final TimeFormatOption timeFormat = new TimeFormatOption("app.TimeFormat");
	public static final NumberFormatOption numberFormat = new NumberFormatOption("app.NumberFormat");

	// fonts
	public static final FontOption fontOption = new FontOption("ui.theme.font")
	{
		public Font defaultValue()
		{
			return Theme.getPanelFont();
		}
	};
}
