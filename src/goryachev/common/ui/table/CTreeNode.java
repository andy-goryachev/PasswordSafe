// Copyright (c) 2007-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;


/**
 * Represent data source for tree.
 */
public abstract class CTreeNode
{
	/** also determines whether getChildren() will be called */
	public abstract boolean isLeaf();

	/** 
	 * children are created once
	 */
	public abstract Object[] getChildren();
	
	public boolean isEditable() { return false; }
	
	// TODO this is weird, remove later
	public void editingStarted() { }
	public void editingStopped() { }
	
	//
	
	private String key;
	private CTreeNode parent;
	
	
	public CTreeNode(CTreeNode parent, String key)
	{
		this.parent = parent;
		this.key = key;
	}
	
	
	public CTreeNode getParent()
	{
		return parent;
	}
	
	
	protected void setParent(CTreeNode parent)
	{
		this.parent = parent;
	}
	

	/** used to discard internal caches upon refresh */
	protected void forceLoadChildren()
	{
	}


	public String[] getPathKeys()
	{
		CList<String> rv = new CList();
		treeKeysRecursive(rv);
		return rv.toArray(new String[rv.size()]);
	}


	protected void treeKeysRecursive(CList<String> list)
	{
		if(parent != null)
		{
			parent.treeKeysRecursive(list);
		}
		
		list.add(getTreeNodeKey());
	}
	

	/** 
	 * Returns a key used to detemine equality.  The key must be unique between the siblings.
	 */
	public final String getTreeNodeKey()
	{
		return key;
	}
}
