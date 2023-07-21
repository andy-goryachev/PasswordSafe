// Copyright © 2005-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;


/**
 * Simple version of CAction which operates with method references
 */
public class XAction
	extends AbstractAction
{
	private Runnable action;
	private ActionEvent event;
	
	public static final XAction DISABLED = new XAction(() -> { })
	{
		public boolean isEnabled() { return false; }
	};


	public XAction(Runnable action)
	{
		this.action = action;
	}


	public XAction(String title)
	{
		super(title);
	}
	
	
	public XAction(String title, Runnable action)
	{
		super(title);
		this.action = action;
	}
	
	
	public XAction(Icon icon, Runnable action)
	{
		this.action = action;
		putValue(Action.SMALL_ICON, icon);
	}


	public void actionPerformed(ActionEvent ev)
	{
		event = ev;
		try
		{
			action.run();
		}
		catch(Exception e)
		{
			Dialogs.err(getSourceWindow(), e);
		}
		finally
		{
			event = null;
		}
	}


	public void fire()
	{
		if(isEnabled())
		{
			actionPerformed(null);
		}
	}


	public void setText(String s)
	{
		setName(s);
	}


	public void setName(String s)
	{
		putValue(Action.NAME, s);
	}


	public String getName()
	{
		return (String)getValue(Action.NAME);
	}


	public void setSelected(boolean on)
	{
		putValue(Action.SELECTED_KEY, on);
	}


	public boolean isSelected()
	{
		return Boolean.TRUE.equals(getValue(Action.SELECTED_KEY));
	}


	public boolean toggleSelected()
	{
		boolean on = !isSelected();
		setSelected(on);
		return on;
	}


	/** returns action source component.  for parent window, use getSourceWindow() */
	public Component getSourceComponent()
	{
		if(event == null)
		{
			return CFocusMonitor.getLastWindow();
		}

		if(event.getSource() instanceof Component)
		{
			return (Component)event.getSource();
		}
		return null;
	}


	/** returns source window even when the actual source component is a JMenuItem (which is not connected to a parent window) */
	public Window getSourceWindow()
	{
		return UI.findParentWindow(getSourceComponent());
	}
}
