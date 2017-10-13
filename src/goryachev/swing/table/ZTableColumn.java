// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.table;
import goryachev.common.util.Parsers;


public class ZTableColumn<T>
{
	public ZTableColumn()
	{
	}
	
	
	public Object getValue(T x)
	{
		return x;
	}
	
	
	public String getText(T x)
	{
		return Parsers.parseString(x);
	}
	
	
	public void decorate(T x)
	{
		// icon, borders, background, ...
	}
}
