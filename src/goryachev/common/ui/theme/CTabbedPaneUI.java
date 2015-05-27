// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;


public class CTabbedPaneUI
	extends BasicTabbedPaneUI
{
	private static Set managingFocusForwardTraversalKeys;
	private static Set managingFocusBackwardTraversalKeys;


	public static void init(UIDefaults defs)
	{
		defs.put("TabbedPane.contentOpaque", Boolean.TRUE);	
	}
	
	
	protected void installDefaults()
	{
		super.installDefaults();
		
		// fixes for OS X
		LookAndFeel.installProperty(tabPane, "opaque", Boolean.FALSE);
		
		tabInsets = new Insets(0, 4, 1, 4);
		selectedTabPadInsets = new Insets(2, 2, 2, 1);
		tabAreaInsets = new Insets(3, 2, 0, 2);
		contentBorderInsets = new Insets(2, 2, 3, 3);
		
		if(managingFocusForwardTraversalKeys == null)
		{
			managingFocusForwardTraversalKeys = new HashSet();
			managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		}
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, managingFocusForwardTraversalKeys);

		if(managingFocusBackwardTraversalKeys == null)
		{
			managingFocusBackwardTraversalKeys = new HashSet();
			managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK));
		}
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, managingFocusBackwardTraversalKeys);

		shadow = Theme.panelBG().darker();
		darkShadow = shadow.darker();
	}


	protected void uninstallDefaults()
	{
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		super.uninstallDefaults();
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new CTabbedPaneUI();
	}


	protected void setRolloverTab(int index)
	{
	}

	
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
	{
		switch(tabPlacement)
		{
		case LEFT:
			x += 1;
			y += 1;
			w -= 1;
			h -= 3;
			break;
		case RIGHT:
			y += 1;
			w -= 2;
			h -= 3;
			break;
		case BOTTOM:
			x += 1;
			w -= 3;
			h -= 1;
			break;
		case TOP:
		default:
			x += 1;
			y += 1;
			w -= 3;
			h -= 1;
			break;
		}

		Color bg = tabPane.getBackgroundAt(tabIndex);
		g.setColor(bg);
		g.fillRect(x, y, w, h);
		
		if(isSelected)
		{
			Color centerColor = new Color(255,255,255,0);
			Color topColor = new Color(255,255,255,150);
			GradientPainter.paintVertical(g, x, y, w, h, centerColor, 75, topColor, 0, null);
		}
	}
	
	
	public void setTabInsets(Insets m)
	{		
		tabInsets = m;
	}
	
	
	public void setSelectedTabPadInsets(Insets m)
	{
		selectedTabPadInsets = m;
	}
	
	
	public void setTabAreaInsets(Insets m)
	{
		tabAreaInsets = m;
	}
	
	
	public void setContentBorderInsets(Insets m)
	{
		contentBorderInsets = m;
	}
}
