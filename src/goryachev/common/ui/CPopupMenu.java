// Copyright © 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AssignMnemonic;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class CPopupMenu
	extends JPopupMenu
{
	public CPopupMenu()
	{
	}
	
	
	public void addNotify()
	{
		AssignMnemonic.assign(this);
		super.addNotify();
	}
	
	
	public JMenuItem add(String s)
	{
		CMenuItem m = new CMenuItem(s);
		m.setEnabled(false);
		return add(m);
	}

	
	public JMenuItem add(String s, Action a)
	{
		CMenuItem m = new CMenuItem(s, a);
		return add(m);
	}
}
