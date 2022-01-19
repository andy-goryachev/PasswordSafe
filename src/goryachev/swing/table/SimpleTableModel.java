// Copyright © 2015-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.table;
import goryachev.common.util.CList;
import java.util.Collection;
import java.util.List;
import javax.swing.table.AbstractTableModel;


public class SimpleTableModel<T>
	extends AbstractTableModel
{
	private CList<T> rows;
	
	
	public SimpleTableModel()
	{
		rows = new CList<>();
	}
	
	
	public SimpleTableModel(List<T> items)
	{
		this.rows = new CList<>(items);
	}
	
	
	public SimpleTableModel(T[] items)
	{
		this.rows = new CList<>(items);
	}
	
	
	public List<T> getItems()
	{
		return new CList(rows);
	}
	
	
	public int getRowCount()
	{
		return rows.size();
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
				return rows.get(row);
			}
		}
		return null;
	}
	
	
	public T getItem(int row)
	{
		if((row >= 0) && (row < getRowCount()))
		{
			return rows.get(row);
		}
		return null;
	}
	
	
	public void add(int ix, T item)
	{
		rows.add(ix, item);
		fireTableRowsInserted(ix, ix);
	}
	
	
	public void add(T item)
	{
		int sz = getRowCount();
		rows.add(item);
		fireTableRowsInserted(sz, sz);
	}
	
	
	public void addAll(T ... items)
	{
		int sz = getRowCount();
		for(T item: items)
		{
			rows.add(item);
		}
		fireTableRowsInserted(sz, sz + items.length);
	}
	
	
	public void addAll(Collection<T> items)
	{
		int sz = getRowCount();
		for(T item: items)
		{
			this.rows.add(item);
		}
		fireTableRowsInserted(sz, sz + items.size());
	}
	
	
	public void setAll(Collection<T> x)
	{
		int sz = getRowCount();
		if(sz > 0)
		{
			rows.clear();
			fireTableRowsDeleted(0, sz-1);
		}
		
		rows.setAll(x);
		sz = getRowCount();
		if(sz > 0)
		{
			fireTableRowsInserted(0, sz-1);
		}
	}
	
	
	public void setAll(T ... items)
	{
		int sz = getRowCount();
		if(sz > 0)
		{
			rows.clear();
			fireTableRowsDeleted(0, sz-1);
		}
		
		rows.setAll(items);
		sz = getRowCount();
		if(sz > 0)
		{
			fireTableRowsInserted(0, sz-1);
		}
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
		rows.add(ix, val);
	}
	
	
	protected void clearNoEvents()
	{
		rows.clear();
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
	
	
	public void remove(T item)
	{
		int ix = indexOf(item);
		remove(ix);
	}
	
	
	public void remove(int ix)
	{
		if((ix < 0) || (ix >= rows.size()))
		{
			return;
		}

		rows.remove(ix);
		fireTableRowsDeleted(ix, ix);
	}
	
	
	public int indexOf(T item)
	{
		return rows.indexOf(item);
	}
}
