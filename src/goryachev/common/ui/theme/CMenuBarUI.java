// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Window;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;


public class CMenuBarUI
	extends BasicMenuBarUI
{
	private Window window;
	private WindowListener windowListener;
	
	
	public static void init(UIDefaults defs)
	{
		defs.put("MenuBarUI", CMenuBarUI.class.getName());
	}


	public static ComponentUI createUI(JComponent x)
	{
		return new CMenuBarUI();
	}


	protected void uninstallListeners()
	{
		if(windowListener != null && window != null)
		{
			window.removeWindowListener(windowListener);
		}
		window = null;
		windowListener = null;
		
		super.uninstallListeners();
	}


//	private void installWindowListener()
//	{
//		if(windowListener == null)
//		{
//			final JMenuBar mb = menuBar;
//			Component c = mb.getTopLevelAncestor();
//			if(c instanceof Window)
//			{
//				window = (Window)c;
//				windowListener = new WindowAdapter()
//				{
//					public void windowActivated(WindowEvent ev)
//					{
//						mb.repaint();
//					}
//
//					public void windowDeactivated(WindowEvent ev)
//					{
//						mb.repaint();
//					}
//				};
//				((Window)c).addWindowListener(windowListener);
//			}
//		}
//	}
}
