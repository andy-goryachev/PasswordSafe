// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.table;


public class ZRowModel<T>
	extends ZTableModel<T,T>
{
	public ZRowModel()
	{
	}
	
	
	protected T getRowValue(T x)
	{
		return x;
	}
}
