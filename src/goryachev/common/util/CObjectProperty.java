// Copyright © 2015-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.ref.WeakReference;


/** fx-like object property capable of dealing with normal and weak listeners. */
public class CObjectProperty<T>
{
	public interface Listener<T>
	{
		public void onPropertyChange(T old, T cur);
	}
	
	//
	
	private T value;
	private CList<Object> listeners;
	
	
	public CObjectProperty(T value)
	{
		set(value);
	}
	
	
	public CObjectProperty()
	{
	}
	
	
	public void set(T v)
	{
		if(CKit.notEquals(value, v))
		{
			T old = value;
			value = v;
			
			fireEvent(old, v);
		}
	}
	
	
	public T get()
	{
		return value;
	}
	
	
	protected void fireEvent(T old, T cur)
	{
		CList<Object> ls = getListeners();
		if(ls != null)
		{
			for(Object x: ls)
			{
				if(x instanceof WeakReference)
				{
					Object v = ((WeakReference)x).get();
					if(v == null)
					{
						// weak reference must have been removed
						synchronized(this)
						{
							listeners.remove(x);
						}
						
						continue;
					}
					else
					{
						x = v;
					}
				}
				
				if(x instanceof Listener)
				{
					try
					{
						((Listener)x).onPropertyChange(old, cur);
					}
					catch(Exception e)
					{
						Log.ex(e);
					}
				}
			}
		}
	}
	
	
	protected synchronized CList<Object> getListeners()
	{
		if(listeners != null)
		{
			return new CList(listeners);
		}
		return null;
	}
	
	
	protected CList<Object> listeners()
	{
		if(listeners == null)
		{
			listeners = new CList();
		}
		return listeners;
	}
	
	
	public synchronized void addListener(Listener<T> li)
	{
		listeners().add(li);
	}
	
	
	public void addListener(boolean fireImmediately, Listener<T> li)
	{
		addListener(li);
		
		if(fireImmediately)
		{
			fireEvent(null, get());
		}
	}
	
	
	public synchronized void addWeakListener(Listener<T> li)
	{
		listeners().add(new WeakReference(li));
	}
	
	
	public void addWeakListener(boolean fireImmediately, Listener<T> li)
	{
		addWeakListener(li);
		
		if(fireImmediately)
		{
			fireEvent(null, get());
		}
	}
}
