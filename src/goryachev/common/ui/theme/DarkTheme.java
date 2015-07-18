// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.util.CPlatform;
import java.awt.Color;
import java.awt.Font;


public class DarkTheme 
	extends ATheme
{
	public DarkTheme(String name)
	{
		super(name);
	}


	protected void customize()
	{
		Color panelBG = new Color(0x1f1f1f);
		
		set(ThemeKey.COLOR_BUTTON_AFFIRM, Color.green);
		set(ThemeKey.COLOR_BUTTON_DESTRUCTIVE, Color.magenta);
		set(ThemeKey.COLOR_FIELD_BG, UI.mix(Color.black, 0.85, panelBG));
		set(ThemeKey.COLOR_FIELD_FG, UI.mix(Color.white, 0.65, Color.black));
		set(ThemeKey.COLOR_FOCUS, new Color(90, 90, 90));
		set(ThemeKey.COLOR_GRID, UI.mix(Color.black, 242, Color.white));
		set(ThemeKey.COLOR_LINE, UI.mix(Color.white, 16, panelBG));
		set(ThemeKey.COLOR_LINK, new Color(0, 102, 0));
		set(ThemeKey.COLOR_PANEL_BG, panelBG);
		set(ThemeKey.COLOR_PANEL_FG, new Color(0xcccccc));
		set(ThemeKey.COLOR_TARGET, UI.mix(Color.black, 0.5, new Color(253, 178, 84)));
		set(ThemeKey.COLOR_TEXT_BG, Color.black);
		set(ThemeKey.COLOR_TEXT_FG, Color.white);
		set(ThemeKey.COLOR_TEXT_SELECTION_BG, UI.mix(Color.green, 0.5, Color.black));
		set(ThemeKey.COLOR_TEXT_SELECTION_FG, Color.white);
		set(ThemeKey.COLOR_TOOL_TIP_BG, new Color(48, 48, 48));
		set(ThemeKey.COLOR_TOOLBAR, UI.mix(Color.white, 0.1, panelBG));
		
		if(CPlatform.isWindows())
		{
			Font f = new Font("Tahoma", Font.PLAIN, 11);
			
			set(ThemeKey.FONT_BASE, f);
			set(ThemeKey.FONT_BOLD, f.deriveFont(Font.BOLD));
			set(ThemeKey.FONT_TITLE, UI.deriveFont(f, true, TITLE_FONT_FACTOR));
		}
	}
}
