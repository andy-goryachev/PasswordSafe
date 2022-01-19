// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.swing.theme.AssignMnemonic;
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
	
	
	/** disabled menu item */
	@Deprecated
	public JMenuItem item(String s)
	{
		CMenuItem m = new CMenuItem(s);
		m.setEnabled(false);
		return add(m);
	}

	
	public CMenuItem item(String s, Action a)
	{
		CMenuItem m = new CMenuItem(s, a);
		add(m);
		return m;
	}
	
	
	public CMenuItem item(String s, Runnable r)
	{
		CMenuItem m = new CMenuItem(s, new XAction(r));
		add(m);
		return m;
	}
	
	
	public void separator()
	{
		addSeparator();
	}
}
