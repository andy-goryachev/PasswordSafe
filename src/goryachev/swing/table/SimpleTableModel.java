// Copyright © 2015-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.table;
import goryachev.common.util.CList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class SimpleTableModel<T>
	extends AbstractTableModel
{
	private CList<T> items;
	
	
	public SimpleTableModel()
	{
		items = new CList<>();
	}
	
	
	public SimpleTableModel(List<T> items)
	{
		this.items = new CList<>(items);
	}
	
	
	public SimpleTableModel(T[] items)
	{
		this.items = new CList<>(items);
	}
	
	
	public int getRowCount()
	{
		return items.size();
	}


	public int getColumnCount()
	{
		return 1;
	}


	public Object getValueAt(int row, int col)
	{
		if((row >= 0) && (row < getRowCount()))
		{
			if(col == 0)
			{
				return items.get(row);
			}
		}
		return null;
	}
	
	
	public T getItem(int row)
	{
		if((row >= 0) && (row < getRowCount()))
		{
			return items.get(row);
		}
		return null;
	}
	
	
	public void add(int ix, T item)
	{
		items.add(ix, item);
	}
	
	
	public void add(T item)
	{
		items.add(item);
	}
	
	
	public void addAll(T ... items)
	{
		for(T item: items)
		{
			this.items.add(item);
		}
	}
	
	
	public void addAll(Collection<T> items)
	{
		for(T item: items)
		{
			this.items.add(item);
		}
	}
	
	
	public void setAll(Collection<T> x)
	{
		items.setAll(x);
	}
	
	
	public CList<T> getSelectedEntries(CTableSelector sel)
	{
		CList<T> list = new CList();
		for(int ix: sel.getSelectedModelRows())
		{
			T v = getItem(ix);
			if(v != null)
			{
				list.add(v);
			}
		}
		return list;
	}
	
	
	protected void addAllNoEvent(T[] items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				insertItemNoEvent(getRowCount(), item);
			}
		}
	}
	
	
	protected void addAllNoEvent(Collection<? extends T> items)
	{
		if(items != null)
		{
			for(T item: items)
			{
				insertItemNoEvent(getRowCount(), item);
			}
		}
	}
	
	
	protected void insertItemNoEvent(int ix, T val)
	{
		items.add(ix, val);
	}
	
	
	protected void clearNoEvents()
	{
		items.clear();
	}
	
	
	public void replaceAll(Collection<? extends T> list)
	{
		clearNoEvents();
		addAllNoEvent(list);
		fireTableDataChanged();
	}


	public void replaceAll(T[] a)
	{
		clearNoEvents();
		addAllNoEvent(a);
		fireTableDataChanged();
	}
	
	
	public T getSelectedEntry(CTableSelector sel)
	{
		int ix = sel.getSelectedModelRow();
		if(ix < 0)
		{
			return null;
		}
		else
		{
			return getItem(ix);
		}
	}
}
