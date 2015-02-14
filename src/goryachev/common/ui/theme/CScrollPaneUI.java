// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import javax.swing.JScrollPane;
import javax.swing.UIDefaults;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollPaneUI;


public class CScrollPaneUI
	extends BasicScrollPaneUI
{
	private static CScrollPaneBorder BORDER = new CScrollPaneBorder();
	
	
	public static void init(UIDefaults defs)
	{
		defs.put("ScrollPaneUI", CScrollPaneUI.class.getName());
		defs.put("ScrollPane.border", BORDER);
		defs.put("ScrollPane.background", Theme.textBG());
		defs.put("Table.scrollPaneBorder", BORDER);
	}
	
	
	protected void installDefaults(JScrollPane sp) 
	{
		super.installDefaults(sp);
		
		if(UI.isNullOrResource(sp.getBorder()))
		{
			sp.setBorder(BORDER);
		}
		
		sp.getViewport().setBackground(Theme.textBG());
	}
	
	
	//
	
	
	public static class CScrollPaneBorder
		extends CBorder //CBevelBorder
		implements UIResource
	{
		public CScrollPaneBorder()
		{
			//super(0.75f, true);
		}
	}
}
