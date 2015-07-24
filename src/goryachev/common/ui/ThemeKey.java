// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;


public enum ThemeKey
{
	// colors
	AFFIRM_BUTTON_COLOR("color.button.affirm"),
	DESTRUCTIVE_BUTTON_COLOR("color.button.destructive"),
	FIELD_BG("color.field.bg"),
	FIELD_FG("color.field.fg"),
	FOCUS_COLOR("color.focus"),
	GRID_COLOR("color.grid"),
	LINE_COLOR("color.line"),
	LINK_COLOR("color.link"),
	PANEL_BG("color.panel.bg"),
	PANEL_FG("color.panel.fg"),
	TARGET_COLOR("color.target"),
	TEXT_BG("color.text.bg"),
	TEXT_FG("color.text.fg"),
	TEXT_SELECTION_BG("color.text.selection.bg"),
	TEXT_SELECTION_FG("color.text.selection.fg"),
	TOOL_TIP_BG("color.tooltip.bg"),
	TOOLBAR_COLOR("color.toolbar"),

	// fonts
	BASE_FONT("font.base"),
	BOLD_FONT("font.bold"),
	MONOSPACED_FONT("font.monospaced"),
	TITLE_FONT("font.title");
	
	//
	
	public final String id;
	
	
	ThemeKey(String id)
	{
		this.id = id;
	}
}