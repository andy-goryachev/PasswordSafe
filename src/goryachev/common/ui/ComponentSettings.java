// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CException;
import goryachev.common.util.CSettings;
import goryachev.common.util.Obj;
import java.awt.Component;
import javax.swing.JComponent;


public abstract class ComponentSettings
{
	public abstract void store(String prefix, CSettings s);
	
	public abstract void restore(String prefix, CSettings s);
	
	//
	
	private static final Object KEY_COMPONENT_SETTINGS = new Obj("KEY_COMPONENT_SETTINGS");
	private String id;
	private ComponentSettings next;
	
	
	public ComponentSettings(String id, JComponent c)
	{
		this.id = id;
		
		Object x = c.getClientProperty(KEY_COMPONENT_SETTINGS);
		if(x == null)
		{
			c.putClientProperty(KEY_COMPONENT_SETTINGS, this);
		}
		else if(x instanceof ComponentSettings)
		{
			((ComponentSettings)x).append(this);
		}
		else
		{
			throw new CException("unable to attach, " + x);
		}
	}
	
	
	protected void append(ComponentSettings s)
	{
		while(s.next != null)
		{
			s = s.next;
		}
		
		s.next = this;
	}
	

	public static ComponentSettings get(Component c)
	{
		if(c instanceof JComponent)
		{
			Object v = ((JComponent)c).getClientProperty(KEY_COMPONENT_SETTINGS);
			if(v instanceof ComponentSettings)
			{
				return (ComponentSettings)v;
			}
		}
		return null;
	}
	
	
	public void storeSettings(String prefix, CSettings s)
	{
		store(prefix + "." + id, s);
		
		if(next != null)
		{
			next.storeSettings(prefix, s);
		}
	}
	
	
	public void restoreSettings(String prefix, CSettings s)
	{
		restore(prefix + "." + id, s);
		
		if(next != null)
		{
			next.restoreSettings(prefix, s);
		}
	}
}
