// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.util.CPlatform;
import java.awt.Color;
import java.awt.Font;


public class ThemeAndy 
	extends ATheme
{
	public ThemeAndy()
	{
		super("Andy");
	}
	
	
	protected void customize()
	{
		Color panelBG = new Color(212, 208, 200);
		
		set(ThemeKey.COLOR_BUTTON_AFFIRM, Color.green);
		set(ThemeKey.COLOR_BUTTON_DESTRUCTIVE, Color.magenta);
		set(ThemeKey.COLOR_FIELD_BG, UI.mix(Color.white, 0.85, panelBG));
		set(ThemeKey.COLOR_FIELD_FG, UI.mix(Color.black, 0.65, Color.white));
		set(ThemeKey.COLOR_FOCUS, new Color(90, 90, 90));
		set(ThemeKey.COLOR_GRID, new Color(242, 242, 242));
		set(ThemeKey.COLOR_LINE, new Color(0xaca8a2));
		set(ThemeKey.COLOR_LINK, new Color(0, 102, 0));
		set(ThemeKey.COLOR_PANEL_BG, panelBG);
		set(ThemeKey.COLOR_PANEL_FG, panelBG.darker());
		set(ThemeKey.COLOR_TARGET, new Color(253, 178, 84));
		set(ThemeKey.COLOR_TEXT_BG, Color.white);
		set(ThemeKey.COLOR_TEXT_FG, Color.black);
		set(ThemeKey.COLOR_TEXT_SELECTION_BG, UI.mix(Color.yellow, 0.9, Color.white));
		set(ThemeKey.COLOR_TEXT_SELECTION_FG, Color.black);
		set(ThemeKey.COLOR_TOOL_TIP_BG, new Color(255, 255, 225));
		set(ThemeKey.COLOR_TOOLBAR, new Color(0xb2aea7));
		
		if(CPlatform.isWindows())
		{
			Font f = new Font("Tahoma", Font.PLAIN, 11);
			
			set(ThemeKey.FONT_BASE, f);
			set(ThemeKey.FONT_BOLD, f.deriveFont(Font.BOLD));
			set(ThemeKey.FONT_TITLE, UI.deriveFont(f, true, TITLE_FONT_FACTOR));
		}
	}
}
