// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;


public class CMenuItemUI
	extends BasicMenuItemUI
{
	public static void init(UIDefaults defs)
	{
		defs.put("MenuItemUI", CMenuItemUI.class.getName());
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new CMenuItemUI();
	}


	protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text)
	{
		ButtonModel model = menuItem.getModel();
		Color old = g.getColor();

		if(model.isEnabled() && (model.isArmed() || (menuItem instanceof JMenu && model.isSelected())))
		{
			g.setColor(selectionForeground);
		}

		ThemeTools.paintText(g, menuItem, textRect, text, 0);

		g.setColor(old);
	}
}
