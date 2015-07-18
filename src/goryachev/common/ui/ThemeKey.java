// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;


public enum ThemeKey
{
	// colors
	COLOR_FIELD_BG("color.field.bg"),
	COLOR_FIELD_FG("color.field.fg"),
	COLOR_FOCUS("color.focus"),
	COLOR_GRID("color.grid"),
	COLOR_LINE("color.line"),
	COLOR_LINK("color.link"),
	COLOR_PANEL_BG("color.panel.bg"),
	COLOR_PANEL_FG("color.panel.fg"),
	COLOR_BUTTON_AFFIRM("color.button.affirm"),
	COLOR_BUTTON_DESTRUCTIVE("color.button.destructive"),
	COLOR_TARGET("color.target"),
	COLOR_TEXT_BG("color.text.bg"),
	COLOR_TEXT_FG("color.text.fg"),
	COLOR_TEXT_SELECTION_BG("color.text.selection.bg"),
	COLOR_TEXT_SELECTION_FG("color.text.selection.fg"),
	COLOR_TOOL_TIP_BG("color.tooltip.bg"),
	COLOR_TOOLBAR("color.toolbar"),

	// fonts
	FONT_BASE("font.base"),
	FONT_BOLD("font.bold"),
	FONT_MONOSPACED("font.monospaced"),
	FONT_TITLE("font.title");
	
	
	//
	
	public final String id;
	
	
	ThemeKey(String id)
	{
		this.id = id;
	}
}