// Copyright © 2022-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.log.Log;
import goryachev.common.util.CList;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;


/**
 * Change Monitor.
 */
public class ChangeMonitor
{
	protected static final Log log = Log.get("ChangeMonitor");
	private final Runnable callback;
	private CList<Runnable> disconnectables = new CList<>();
	private DelayedActionSwing action;
	
	
	public ChangeMonitor(int delay, Runnable callback)
	{
		this.callback = callback;
		
		if(delay > 0)
		{
			action = new DelayedActionSwing(delay, this::fireEvent);
		}
	}
	
	
	public ChangeMonitor(Runnable callback)
	{
		this(0, callback);
	}
	
	
	public void disconnectAll()
	{
		for(Runnable r: disconnectables)
		{
			r.run();
		}
		disconnectables.clear();
	}
	
	
	protected void fireEvent()
	{
		log.debug();
		
		try
		{
			callback.run();
		}
		catch(Throwable e)
		{
			log.error(e);
		}
	}
	
	
	public void trigger()
	{
		if(action == null)
		{
			fireEvent();
		}
		else
		{
			action.trigger();
		}
	}
	
	
	public void listenAll(JComponent ... items)
	{
		for(JComponent c: items)
		{
			if(c instanceof JCheckBox)
			{
				listen((JCheckBox)c);
			}
			else if(c instanceof JComboBox)
			{
				listen((JComboBox)c);
			}
			else if(c instanceof JTextComponent)
			{
				listen((JTextComponent)c);
			}
			else
			{
				throw new Error("don't know how to listen to " + c.getClass().getSimpleName()); 
			}
		}
	}
	
	
	public void listen(JCheckBox c)
	{
		// TODO or item listener?
		ChangeListener li = new ChangeListener()
		{
			public void stateChanged(ChangeEvent ev)
			{
				trigger();
			}
		};
		
		c.addChangeListener(li);
		
		disconnectables.add(() ->
		{
			c.removeChangeListener(li);
		});
	}
	
	
	public void listen(JComboBox c)
	{
		ItemListener li = new ItemListener()
		{
			public void itemStateChanged(ItemEvent ev)
			{
				trigger();
			}
		};
		
		c.addItemListener(li);
		
		disconnectables.add(() ->
		{
			c.removeItemListener(li);
		});
	}
	
	
	public void listen(JTextComponent c)
	{
		DocumentListener li = new DocumentListener()
		{
			public void removeUpdate(DocumentEvent ev)
			{
				trigger();
			}
			
			
			public void insertUpdate(DocumentEvent ev)
			{
				trigger();
			}
			
			
			public void changedUpdate(DocumentEvent ev)
			{
				trigger();
			}
		};
		
		PropertyChangeListener pli = new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent ev)
			{
				Object old = ev.getOldValue();
				if(old instanceof Document)
				{
					((Document)old).removeDocumentListener(li);
				}
				
				Object cur = ev.getNewValue();
				if(cur instanceof Document)
				{
					((Document)cur).addDocumentListener(li);
				}
			}
		};
		
		Document doc = c.getDocument();
		doc.addDocumentListener(li);

		c.addPropertyChangeListener("document", pli);
		
		disconnectables.add(() ->
		{
			c.removePropertyChangeListener("document", pli);
			c.getDocument().removeDocumentListener(li);
		});
		
		c.setDocument(new PlainDocument()); // FIX
	}
}
