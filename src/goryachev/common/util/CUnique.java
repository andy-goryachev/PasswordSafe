// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Collection;
import java.util.Iterator;


public class CUnique<T>
	implements Iterable<T>
{
	private final CMap<T,Object> items;
	
	
	public CUnique()
	{
		this(32);
	}
	
	
	public CUnique(Collection<T> items)
	{
		this(items == null ? 32 : items.size());
		addAll(items);
	}
	
	
	public CUnique(CUnique<T> items)
	{
		this(items == null ? 32 : items.size());
		addAll(items);
	}
	
	
	public CUnique(T[] items)
	{
		this(items == null ? 32 : items.length);
		addAll(items);		
	}
	
	
	public CUnique(T item)
	{
		this(32);
		put(item);
	}
	
	
	public CUnique(int capacity)
	{
		items = new CMap(capacity);
	}
	
	
//	/** Returns true if item is already there */
	// removed because of the wrong return value.  use put() now
//	public boolean add(T item)
//	{
//		return items.put(item, Boolean.TRUE) != null;
//	}
	
	
	/** Returns true if item an item has been added, false if it was already there */
	public boolean put(T item)
	{
		return items.put(item, Boolean.TRUE) == null;
	}
	
	
	/** Returns true if an item is new, and adds this item to the map.  This method is the opposite of add() */ 
	public boolean isUnique(T item)
	{
		return items.put(item, Boolean.TRUE) == null;
	}
	
	
	public CList<T> getItems()
	{
		return new CList(items.keySet());
	}
	
	
	public boolean contains(T item)
	{
		return items.containsKey(item);
	}
	
	
	public int size()
	{
		return items.size();
	}
	
	
	/** Removes an item.  Returns true if an item was present in the collection */
	public boolean remove(T item)
	{
		return (items.remove(item) != null);
	}
	
	
	public void removeAll(T[] items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				remove(item);
			}
		}
	}
	
	
	public void removeAll(Iterable<T> items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				remove(item);
			}
		}
	}

	
	public void removeAll(Collection<T> items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				remove(item);
			}
		}
	}
	

	public Iterator<T> iterator()
	{
		return new CList(items.keySet()).iterator();
	}
	
	
	public CList<T> asList()
	{
		return new CList(items.keySet());
	}
	
	
	public void clear()
	{
		items.clear();
	}
	
	
	public void addAll(Collection<T> items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				put(item);
			}
		}
	}
	
	
	public void addAll(CUnique<T> items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				put(item);
			}
		}
	}
	
	
	public void addAll(T[] items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				put(item);
			}
		}
	}
	
	
	public void replaceAll(Collection<T> items)
	{
		clear();
		addAll(items);
	}
	
	
	public void replaceAll(T[] items)
	{
		clear();
		addAll(items);
	}
	
	
	/** returns a single item, or null if zero or more than one items is present */
	public T getSingleItem()
	{
		if(size() == 1)
		{
			return items.keySet().iterator().next();
		}
		return null;
	}
}
