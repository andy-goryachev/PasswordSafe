// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import javax.swing.JScrollPane;
import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicScrollPaneUI;


public class AgScrollPaneUI
	extends BasicScrollPaneUI
{
	private static CBorder BORDER = new CBorder();
	
	
	public static void init(UIDefaults defs)
	{
		defs.put("ScrollPaneUI", AgScrollPaneUI.class.getName());
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
}
