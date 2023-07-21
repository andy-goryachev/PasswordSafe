// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.swing.theme.AssignMnemonic;
import java.awt.Component;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;


public class CMenuBar
	extends JMenuBar
{
	public CMenuBar()
	{
	}
	
	
	public void addNotify()
	{
		AssignMnemonic.assign(this);
		super.addNotify();
	}
	
	
	public JMenu lastMenu()
	{
		for(int i=getComponentCount()-1; i>=0; --i)
		{
			Component c = getComponent(i);
			if(c instanceof JMenu)
			{
				return (JMenu)c;
			}
		}
		return null;
	}
	
	
	public void fill()
	{
		add(Box.createHorizontalGlue());
	}
	
	
	public CMenu menu(String text)
	{
		CMenu m = new CMenu(text);
		add(m);
		return m;
	}
	
	
	public CMenuItem item(String text, Action a)
	{
		CMenuItem m = new CMenuItem(text, a);
		lastMenu().add(m);
		return m;
	}
	
	
	public CMenuItem item(String text, Accelerator acc, Action a)
	{
		CMenuItem m = new CMenuItem(text, acc, a);
		lastMenu().add(m);
		return m;
	}
	
	
	/** creates a disabled menu item for development purposes */
	@Deprecated
	public CMenuItem item(String text)
	{
		CMenuItem m = new CMenuItem(text, XAction.DISABLED);
		lastMenu().add(m);
		return m;
	}
	
	
	/** creates a disabled checkbox menu item for development purposes */
	@Deprecated
	public CCheckBoxMenuItem checkBoxItem(String text)
	{
		CCheckBoxMenuItem m = new CCheckBoxMenuItem(text, XAction.DISABLED);
		lastMenu().add(m);
		return m;
	}
	
	
	public void separator()
	{
		lastMenu().addSeparator();
	}
}
