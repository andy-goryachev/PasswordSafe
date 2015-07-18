// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;
import goryachev.common.ui.Theme;


public class AgSeparatorUI 
	extends BasicSeparatorUI
{
	public static ComponentUI createUI(JComponent c)
	{
		return new AgSeparatorUI();
	}


	public static void init(UIDefaults defs)
	{
		defs.put("SeparatorUI", AgSeparatorUI.class.getName());
		defs.put("Separator.background", Theme.brighter(Theme.panelBG())); // FIX
		defs.put("Separator.foreground", Theme.darker(Theme.panelBG()));
		defs.put("Separator.highlight", Theme.brighter(Theme.panelBG()));
		defs.put("Separator.shadow", Theme.darker(Theme.panelBG()));
	}


	public AgSeparatorUI()
	{
	}
}
