// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.table;
import javax.swing.table.TableModel;


public interface ZRowFilter
{
	boolean include(TableModel m, int ix);
}
