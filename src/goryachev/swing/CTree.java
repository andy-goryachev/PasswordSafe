// Copyright © 2009-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


public class CTree
	extends JTree
{
	public void handleDroppedNodes(Object[] sel) { }
	
	//
	
	public CTree()
	{ }
	
	
	public CTree(TreeNode root)
	{
		super(root);
	}
	
	
	public CTree(TreeNode root, boolean asksAllowsChildren)
	{
		super(root, asksAllowsChildren);
	}
	
	
	public CTree(TreeModel model)
	{
		super(model);
	}
	
	
	public void setRoot(TreeNode root)
	{
		setModel(new DefaultTreeModel(root, false));
	}
	
	
	public void expandAll()
	{
		int row = 0;
		while(row < getRowCount())
		{
			expandRow(row);
			row++;
		}
	}
	
	
	public Object[] getSelectedNodes()
	{
		TreePath[] ps = getSelectionPaths();
		if(ps == null)
		{
			return new Object[0];
		}
		
		int sz = ps.length;
		Object[] sel = new Object[sz];
		
		for(int i=0; i<sz; i++)
		{
			sel[i] = ps[i].getLastPathComponent();
		}
		
		return sel;
	}
	
	
	public void selectFirst()
	{
		int rows = getRowCount();
		if(rows > 0)
		{
			setSelectionRow(0);
		}
	}
}
