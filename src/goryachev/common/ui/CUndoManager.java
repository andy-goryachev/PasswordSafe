// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CException;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;


public class CUndoManager
	extends UndoManager
{
	public final LocalUndoAction localUndoAction = new LocalUndoAction();
	public final LocalRedoAction localRedoAction = new LocalRedoAction();
	public static final UndoAction undoAction = new UndoAction();
	public static final RedoAction redoAction = new RedoAction();
	protected static CUndoManager last;
	static
	{
		init();
	}
	

	public CUndoManager()
	{
	}
	
	
	private static void init()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent ev)
			{
				JTextComponent x = CFocusMonitor.getLastTextComponent();
				CUndoManager u = getUndoManager(x);
				if(u != last)
				{
					undoAction.setManager(u);
					redoAction.setManager(u);
					last = u;
				}
			}
		});
	}
	
	
	/** each JTextComponent gets its own instance of CUndoManager */
	public static void monitor(Object ... xs)
	{
		for(Object x: xs)
		{
			CUndoManager u = getUndoManager(x);
			if(u == null)
			{
				if(x instanceof JTextComponent)
				{
					Document d = ((JTextComponent)x).getDocument();
					d.addUndoableEditListener(new CUndoManager());
				}
				else if(x instanceof AbstractDocument)
				{
					((AbstractDocument)x).addUndoableEditListener(new CUndoManager());
				}
				else
				{
					throw new CException("unable to attach undo manager to " + CKit.simpleName(x));
				}
			}
		}
	}
	
	
	public static void clear(Object ... xs)
	{
		for(Object x: xs)
		{
			CUndoManager u = getUndoManager(x);
			if(u != null)
			{
				u.discardAllEdits();
			}
		}
	}
	
	
	protected static CUndoManager getUndoManager(Object x)
	{
		if(x instanceof JTextComponent)
		{
			Document d = ((JTextComponent)x).getDocument();
			if(d instanceof AbstractDocument)
			{
				for(UndoableEditListener ul: ((AbstractDocument)d).getUndoableEditListeners())
				{
					if(ul instanceof CUndoManager)
					{
						return (CUndoManager)ul;
					}
				}
			}
		}
		else if(x instanceof AbstractDocument)
		{
			for(UndoableEditListener ul: ((AbstractDocument)x).getUndoableEditListeners())
			{
				if(ul instanceof CUndoManager)
				{
					return (CUndoManager)ul;
				}
			}
		}
		return null;
	}

	
	public synchronized boolean addEdit(UndoableEdit ed)
	{
		boolean rv = super.addEdit(ed);
		update();
		return rv;
	}


	public synchronized void discardAllEdits()
	{
		super.discardAllEdits();
		update();
	}


	protected void update()
	{
		localUndoAction.updateUndoState();
		localRedoAction.updateRedoState();
	}
	

	//
	
	
	public class LocalRedoAction
	    extends CAction
	{
		public LocalRedoAction()
		{
			super(Menus.Redo);
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
	
	
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				redo();
			}
			catch(Exception e)
			{
				Log.err(e);
				UI.beep();
			}
			
			update();
		}
	
	
		protected void updateRedoState()
		{
			if(canRedo())
			{
				setEnabled(true);
				setName(getRedoPresentationName());
			}
			else
			{
				setEnabled(false);
				setName(Menus.Redo);
			}
		}
	}
	
	
	//
	
	
	public class LocalUndoAction
		extends CAction
	{
		public LocalUndoAction()
		{
			super(Menus.Undo);
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
	
	
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				undo();
			}
			catch(Exception e)
			{
				Log.err(e);
				UI.beep();
			}
			
			update();
		}
	
	
		protected void updateUndoState()
		{
			if(canUndo())
			{
				setEnabled(true);
				setName(getUndoPresentationName());
			}
			else
			{
				setEnabled(false);
				setName(Menus.Undo);
			}
		}
	}
	
	
	//
	
	
	public abstract static class AbstractGlobalAction
		extends CAction 
		implements PropertyChangeListener
	{
		protected abstract Action getAction(CUndoManager m);
		
		//
		
		private Action action;
		
		
		public AbstractGlobalAction()
		{
			setEnabled(false);
		}
		
		
		public void action()
		{
			// not called
		}
		
		
		public void actionPerformed(ActionEvent ev)
		{
			if(action != null)
			{
				action.actionPerformed(ev);
			}
		}
	
	
		public void propertyChange(PropertyChangeEvent ev)
		{
			String id = ev.getPropertyName();
//			if("Name".equals(id))
//			{
//				update();
//			}
//			else 
			if("enabled".equals(id))
			{
				update();
			}
		}
		
		
		protected void setManager(CUndoManager m)
		{
			Action a = getAction(m);
			if(action != a)
			{
				// change action
				if(action != null)
				{
					action.removePropertyChangeListener(this);
				}
				
				action = a;
				
				if(action != null)
				{
					action.addPropertyChangeListener(this);
				}
				
				update();
			}
		}
		
		
		protected void update()
		{
			if(action != null)
			{
				// TODO set tooltip instead
				//setText((String)action.getValue(Action.NAME));
				setEnabled(action.isEnabled());
			}
		}
	}
	
	
	//
	
	
	public static class UndoAction
		extends AbstractGlobalAction
	{
		protected Action getAction(CUndoManager m)
		{
			return m == null ? null : m.localUndoAction;
		}
	}
	
	
	//
	
	
	public static class RedoAction
		extends AbstractGlobalAction
	{
		protected Action getAction(CUndoManager m)
		{
			return m == null ? null : m.localRedoAction;
		}
	}
}
