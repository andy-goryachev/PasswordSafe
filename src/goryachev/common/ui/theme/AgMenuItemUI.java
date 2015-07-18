// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;


public class AgMenuItemUI
	extends BasicMenuItemUI
{
	public static void init(UIDefaults d)
	{
		d.put("MenuItemUI", AgMenuItemUI.class.getName());
		//defs.put("MenuItem.acceleratorFont", Theme.plainFont());
		d.put("MenuItem.acceleratorForeground", Theme.panelFG());
		d.put("MenuItem.acceleratorSelectionForeground", Theme.panelFG());
		d.put("MenuItem.background", Theme.panelBG());
		d.put("MenuItem.foreground", Theme.textFG());
		d.put("MenuItem.selectionBackground", Theme.textSelectionBG());
		d.put("MenuItem.selectionForeground", Theme.textSelectionFG());
		d.put("MenuItem.disabledForeground", Theme.fieldFG());
        
		d.put("MenuItem.border", new CBorder(2));
		d.put("MenuItem.borderPainted", Boolean.TRUE);
		d.put("MenuItem.margin", new Insets(2, 2, 2, 2));
		
		/*
		MenuItem.border = javax.swing.plaf.metal.MetalBorders$MenuItemBorder(t=2,l=2,b=2,r=2)
		MenuItem.disabledForeground = ColorUIResource(999999)
		MenuItem.foreground = PrintColorUIResource(333333)
		*/
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new AgMenuItemUI();
	}


	protected void paintText(Graphics g, JMenuItem mi, Rectangle r, String text)
	{
		ButtonModel m = mi.getModel();
		Color old = g.getColor();

		if(m.isEnabled() && (m.isArmed() || (mi instanceof JMenu && m.isSelected())))
		{
			g.setColor(selectionForeground);
		}

		ThemeTools.paintText(g, mi, r, text, 0);

		g.setColor(old);
	}
}
