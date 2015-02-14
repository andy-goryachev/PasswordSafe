// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Simple item-based table model similar to old CTableModel
 */
public class ZModel<V>
	extends ZTableModelCommon
	implements Iterable<V>
{
	private CList<V> rows = new CList();
	
	
	public ZModel()
	{
	}
	
	
	public ZColumnInfo addColumn(String name, ZColumnHandler<? extends V> h)
	{
		ZColumnInfo zi = addColumnPrivate();
		zi.name = name;
		zi.handler = h;
		return zi;
	}
	
	
	public ZColumnInfo addColumn(String name, int preferredWidth, ZColumnHandler<? extends V> h)
	{
		ZColumnInfo zi = addColumnPrivate();
		zi.name = name;
		zi.handler = h;
		zi.pref = preferredWidth;
		return zi;
	}
	
	
	public void addItem(V item)
	{
		insertItem(getRowCount(), item);
	}


	public void insertItem(int ix, V item)
	{
		insertItemNoEvent(ix, item);
		fireTableRowsInserted(ix, ix);
	}
	
	
	public void insertItems(int start, Collection<? extends V> items)
	{
		if(items != null)
		{
			int sz = items.size();
			if(sz > 0)
			{
				int ix = start;
				for(V item: items)
				{
					insertItemNoEvent(ix, item);
					ix++;
				}
			
				fireTableRowsInserted(start, start + sz - 1);
			}
		}
	}
	
	
	public void insertItems(int start, V[] items)
	{
		if(items != null)
		{
			int sz = items.length;
			if(sz > 0)
			{
				int ix = start;
				for(V item: items)
				{
					insertItemNoEvent(ix, item);
					ix++;
				}
			
				fireTableRowsInserted(start, start + sz - 1);
			}
		}
	}


	protected void insertItemNoEvent(int ix, V val)
	{
		// TODO elastic?
		rows.add(ix, val);
	}
	

	public Iterator<V> iterator()
	{
		return new Iterator<V>()
		{
			int ix;
			
			public boolean hasNext()
			{
				return (ix < size());
			}

			public V next()
			{
				return getItem(ix++);
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}


	protected void clearNoEvents()
	{
		rows.clear();
	}


	protected void removeNoEvents(int ix)
	{
		if((ix >= 0) && (ix < size()))
		{
			rows.remove(ix);
		}
	}


	protected void removeNoEvents(int first, int lastExclusive)
	{
		for(int i=lastExclusive-1; i>=first; i--)
		{
			rows.remove(i);
		}
	}


	public int getRowCount()
	{
		return rows.size();
	}
	
	
	public boolean isCellEditable(int row, int col)
	{
		V item = getItem(row);
		return columnHandler(col).isCellEditable(item); // && super.isCellEditable(row, col);
	}

	
	public V getItem(int row)
	{
		if((row >= 0) && (row < size()))
		{
			return rows.get(row);
		}		
		return null;
	}
	
	
	public void setItem(int row, V item)
	{
		setItemNoEvent(row, item);
		fireTableRowUpdated(row);
	}
	
	
	protected void setItemNoEvent(int row, V item)
	{
		rows.set(row, item);
	}
	
	
	protected ZColumnHandler<V> columnHandler(int col)
	{
		return (ZColumnHandler<V>)getColumnInfo(col).handler;
	}
	

	public Object getValueAt(int row, int col)
	{
		if(row < 0)
		{
			return null;
		}
		else if(col < 0)
		{
			return null;
		}
		
		V item = getItem(row);
		return columnHandler(col).getCellValue(item);
	}


	public void setValueAt(Object val, int row, int col)
	{
		V item = getItem(row);
		columnHandler(col).setCellValue(item, val);
	}


	public void replaceAll(Collection<? extends V> list)
	{
		clearNoEvents();
		addAllNoEvent(list);
		fireTableDataChanged();
	}


	public void replaceAll(V[] a)
	{
		clearNoEvents();
		addAllNoEvent(a);
		fireTableDataChanged();
	}
	
	
	public void addAll(Collection<? extends V> items)
	{
		insertItems(size(), items);
	}
	
	
	public void addAll(V[] items)
	{
		insertItems(size(), items);
	}


	protected void addAllNoEvent(Collection<? extends V> items)
	{
		if(items != null)
		{
			for(V item: items)
			{
				insertItemNoEvent(size(), item);
			}
		}
	}


	protected void addAllNoEvent(V[] items)
	{
		if(items != null)
		{
			for(V item: items)
			{
				insertItemNoEvent(size(), item);
			}
		}
	}
	
	
	public CList<V> getSelectedEntries(CTableSelector sel)
	{
		CList<V> list = new CList();
		for(int ix: sel.getSelectedModelRows())
		{
			V v = getItem(ix);
			if(v != null)
			{
				list.add(v);
			}
		}
		return list;
	}
	
	
	public void setSelectedEntries(CTableSelector sel, CList<V> items)
	{
		if(items != null)
		{
			int sz = items.size();
			int[] rows = new int[sz];
			for(int i=0; i<sz; i++)
			{
				V item = items.get(i);
				int ix = indexOfKey(item);
				rows[i] = ix;
			}
			
			sel.setSelectedModelRows(rows);
		}
	}
	
	
	public void setSelectedEntry(CTableSelector sel, V item)
	{
		int ix = indexOfKey(item);
		sel.setSelectedModelRow(ix);
	}
	
	
	public V getSelectedEntry(CTableSelector sel)
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
	
	
	public void refreshAll()
	{
		int last = size() - 1;
		if(last >= 0)
		{
			fireTableRowsUpdated(0, last);
		}
	}
	
	
	public void refreshRow(int modelRow)
	{
		fireTableRowsUpdated(modelRow, modelRow);
	}
	
	
	public int indexOfKey(V item)
	{
		if(item == null)
		{
			return -1;
		}
		
		int sz = getRowCount();
		for(int i=0; i<sz; i++)
		{
			if(item.equals(getItem(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public V removeItem(V item)
	{
		int ix = indexOfKey(item);
		if(ix >= 0)
		{
			item = getItem(ix);
			remove(ix);
			return item;
		}
		else
		{
			return null;
		}
	}
	
	
	public CList<V> getItems()
	{
		return new CList(rows);
	}


	public void fireTableRowUpdated(int row)
	{
		fireTableRowsUpdated(row, row);
	}


	public void fireAllRowsUpdated()
	{
		int sz = size();
		if(sz > 0)
		{
			fireTableRowsUpdated(0, sz - 1);
		}
	}
	
	
	public void fireItemUpdated(V item)
	{
		int ix = indexOfKey(item);
		if(ix >= 0)
		{
			fireTableRowUpdated(ix);
		}
	}
}
