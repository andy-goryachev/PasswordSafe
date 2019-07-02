// Copyright © 2008-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.swing.theme.AssignMnemonic;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


public class CMenu
	extends JMenu
{
	public CMenu(Action a, ImageIcon icon, String text)
	{
		super(a);
		setIcon(icon);
		setText(text);
		
		UI.setMnemonic(this);
	}
	
	
	public CMenu(String text, Action a)
	{
		super(a);
		setText(text);
		
		UI.setMnemonic(this);
	}
	
	
	public CMenu(String text)
	{
		super(text);
		
		UI.setMnemonic(this);
	}
	
	
	public CMenu(Action a)
	{
		super(a);
	}
	
	
	public void addNotify()
	{
		AssignMnemonic.assign(this);
		super.addNotify();
	}
	
	
	protected PropertyChangeListener createActionChangeListenerLocal(JMenuItem mi)
	{
		return super.createActionChangeListener(mi);
	}


	protected JMenuItem createActionComponent(Action a)
	{
		CMenuItem mi = new CMenuItem()
		{
			protected PropertyChangeListener createActionPropertyChangeListener(Action a)
			{
				PropertyChangeListener li = createActionChangeListenerLocal(this);
				if(li == null)
				{
					li = super.createActionPropertyChangeListener(a);
				}
				return li;
			}
		};
		mi.setHorizontalTextPosition(JButton.TRAILING);
		mi.setVerticalTextPosition(JButton.CENTER);
		return mi;
	}
	
	
	public JMenuItem add(String s)
	{
		return super.add(new CMenuItem(Img.EMPTY, s, CAction.TODO));
	}
	
	
	public int indexOfMenu(String title)
	{
		int sz = getMenuComponentCount();
		for(int i=0; i<sz; i++)
		{
			Component c = getMenuComponent(i);
			if(c instanceof JMenuItem)
			{
				JMenuItem mi = (JMenuItem)c;
				if(title.equals(mi.getText()))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public CMenuItem item(String text, Action a)
	{
		CMenuItem m = new CMenuItem(text, a);
		add(m);
		return m;
	}
	
	
	public CMenuItem item(String text, Accelerator acc, Action a)
	{
		CMenuItem m = new CMenuItem(text, acc, a);
		add(m);
		return m;
	}
	
	
	/** creates a disabled menu item for development purposes */
	@Deprecated
	public CMenuItem item(String text)
	{
		CMenuItem m = new CMenuItem(text, XAction.DISABLED);
		add(m);
		return m;
	}
	

	public void separator()
	{
		addSeparator();
	}
}
