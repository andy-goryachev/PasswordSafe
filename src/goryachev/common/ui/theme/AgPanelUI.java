// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;


public class AgPanelUI
	extends BasicPanelUI
{
	private final static AgPanelUI panelUI = new AgPanelUI();
	
	
	public AgPanelUI()
	{
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("PanelUI", AgPanelUI.class.getName());
		d.put("PanelUI.foreground", Theme.panelFG());
		d.put("PanelUI.background", Theme.panelBG());
	}
	
	
	public static ComponentUI createUI(JComponent b)
	{
		return panelUI;
	}
}
