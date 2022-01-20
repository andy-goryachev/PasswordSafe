// Copyright © 2008-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.util.CKit;
import goryachev.common.util.HasProperty;
import java.awt.event.ItemEvent;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;


// TODO extend popup window to fit the content
public class CComboBox
	extends JComboBox
{
	public CComboBox()
	{
		init();
	}
	
	
	public CComboBox(Object ... items)
	{
		super(items);
		init();
	}

	
	public CComboBox(Collection<?> items)
	{
		super(items == null ? new Object[0] : items.toArray());
		init();
	}
	
	
	private void init()
	{
		setMaximumRowCount(32);
	}
	
	
	public void selectFirst()
	{
		if(getItemCount() > 0)
		{
			setSelectedIndex(0);
		}
	}
	
	
	public void select(Object item)
	{
		setSelectedItem(item);
	}

	
	public void replaceAll(Object[] items)
	{
		setModel(new DefaultComboBoxModel(items));
	}
	
	
	public void replaceAll(Collection<?> items)
	{
		Object[] a = (items == null ? new Object[0] : items.toArray());
		setModel(new DefaultComboBoxModel(a));
	}
	
	
	public void addItems(Object[] items)
	{
		for(Object item: items)
		{
			addItem(item);
		}
	}
	
	
	public void addItems(Iterable<?> items)
	{
		for(Object item: items)
		{
			addItem(item);
		}
	}
	
	
	public void selectByProperty(String value)
	{
		if(value != null)
		{
			int sz = getModel().getSize();
			for(int i=0; i<sz; i++)
			{
				Object x = getModel().getElementAt(i);
				if(x instanceof HasProperty)
				{
					if(value.equals(((HasProperty)x).getProperty()))
					{
						setSelectedIndex(i);
						return;
					}
					else if(value.equals(x))
					{
						setSelectedIndex(i);
						return;
					}
				}
			}
			
			// this is questionable
			if(isEditable())
			{
				setSelectedItem(value);
			}
		}
	}
	
	
	public String getSelectedProperty()
	{
		Object x = getSelectedItem();
		if(x instanceof HasProperty)
		{
			return ((HasProperty)x).getProperty();
		}
		else 
		{
			// this is questionable
			return x == null ? null : x.toString();
		}
	}
	
	
	public JComponent getEditorComponent()
	{
		return (JComponent)getEditor().getEditorComponent();
	}
	
	
	public Object getCurrentItem()
	{
		if(isEditable())
		{
			return getEditor().getItem();
		}
		else
		{
			return getSelectedItem();
		}
	}
	
	
	/** performs toString conversion of getCurrentItem() */
	public String getSelectedString()
	{
		Object rv = getCurrentItem();
		return CKit.toStringOrNull(rv);
	}
	
	
	public void addSelectionListener(Runnable r)
	{
		addItemListener((ItemEvent ev) ->
		{
			if(ev.getStateChange() == ItemEvent.SELECTED)
			{
				r.run();
			}
		});
	}
	
	
	public void selectByString(Object value)
	{
		if(value != null)
		{
			String val = value.toString();
			
			int sz = getModel().getSize();
			for(int i=0; i<sz; i++)
			{
				String v = getModel().getElementAt(i).toString();
				if(val.equals(v))
				{
					setSelectedIndex(i);
					return;
				}
			}
		}
	}
}
