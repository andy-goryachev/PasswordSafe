// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.i18n.Menus;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;


/* 
 * Standard popup menu with Cut, Copy, Paste which get smartly disabled if the component does not support corresponding operations 
 */ 
public class StandardTextPopupMenu
	extends CPopupMenu
{
	protected static final XAction cutAction = new XAction(StandardTextPopupMenu::actionCut);
	protected static final XAction copyAction = new XAction(StandardTextPopupMenu::actionCopy);
	protected static final XAction pasteAction = new XAction(StandardTextPopupMenu::actionPaste);
	protected static final StandardTextPopupMenu INSTANCE = new StandardTextPopupMenu();
	private static JTextComponent caller;
	
	
	public StandardTextPopupMenu()
	{
		add(new CMenuItem(Menus.Cut, cutAction));
		add(new CMenuItem(Menus.Copy, copyAction));
		add(new CMenuItem(Menus.Paste, pasteAction));
	}
	

	public static void addTo(JComponent c)
	{
		new CPopupMenuController(c)
		{
			public JPopupMenu constructPopupMenu()
			{
				return INSTANCE;
			}
		};
	}
	
	
	public static void addTo(JComponent ... cs)
	{
		for(JComponent c: cs)
		{
			addTo(c);
		}
	}
	
		
	protected static void actionCut()
	{
		caller.cut();
	}

	
	protected static void actionCopy()
	{
		caller.copy();
	}

	
	protected static void actionPaste()
	{
		caller.paste();
	}
	
	
	public void show(Component invoker, int x, int y)
	{
		updateActions(invoker);
		super.show(invoker, x, y);
	}


	protected void updateActions(Component c)
	{
		boolean editable = false;
		boolean copy = false;
		
		if(c instanceof JTextComponent)
		{
			JTextComponent tc = (JTextComponent)c;
			editable = tc.isEditable();
			copy = true;
			
			caller = tc;
		}
		
		cutAction.setEnabled(editable);
		copyAction.setEnabled(copy);
		pasteAction.setEnabled(editable);
	}
}


