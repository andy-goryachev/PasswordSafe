// Copyright © 2010-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.table;


public interface CTreeTableListener
{
	public void treeTableWillExpand(CTreeTable tree, int tableRow) throws Exception;


	public void treeTableWillCollapse(CTreeTable tree, int tableRow) throws Exception;
}
