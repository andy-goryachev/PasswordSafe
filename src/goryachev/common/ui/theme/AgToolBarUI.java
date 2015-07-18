// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;


public class AgToolBarUI
	extends BasicToolBarUI
{
	public AgToolBarUI()
	{
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("ToolBarUI", AgToolBarUI.class.getName());
		d.put("ToolBarUI.background", Theme.toolbarColor());
		d.put("ToolBarUI.foreground", Theme.panelFG());
		d.put("ToolBarUI.dockingBackground", Theme.panelBG());
		d.put("ToolBarUI.dockingForeground", Theme.panelFG());
		d.put("ToolBarUI.floatingBackground", Theme.panelBG());
		d.put("ToolBarUI.floatingForeground", Theme.panelFG());
		d.put("ToolBar.isRollover", Boolean.FALSE);
		
//		ToolBar.ancestorInputMap = InputMapUIResource
//		ToolBar.border = javax.swing.plaf.metal.MetalBorders$ToolBarBorder
//		ToolBar.borderColor = ColorUIResource(CCCCCC)
//		ToolBar.darkShadow = ColorUIResource(7A8A99)
//		ToolBar.highlight = ColorUIResource(FFFFFF)
//		ToolBar.isRollover = true
//		ToolBar.light = ColorUIResource(FFFFFF)
//		ToolBar.nonrolloverBorder = javax.swing.border.CompoundBorder
//		ToolBar.rolloverBorder = javax.swing.border.CompoundBorder
//		ToolBar.separatorSize = DimensionUIResource(w=10.0,h=10.0)
//		ToolBar.shadow = ColorUIResource(B8CFE5)
	}
	
	
	public static ComponentUI createUI(JComponent b)
	{
		return new AgToolBarUI();
	}
}
