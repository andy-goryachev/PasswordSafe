// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;


/** 
 * A CTreeNode which holds the list of its children.
 * This class normally provides a cached mode of operation when the children
 * are added once.  For a non-caching mode, override loadChildren() method to reload 
 * children each time getChildren() is called (make sure to call clearChildren());
 */
public abstract class CTreeNode2
    extends CTreeNode
{
	/** override in non-caching mode */
	protected void loadChildren() { }
	
	//
	
	private CList<CTreeNode> children;

	
	public CTreeNode2(CTreeNode parent, String key)
	{
		super(parent, key);
	}
	

	public final Object[] getChildren()
	{
		return children().toArray();
	}
	
	
	protected CList<CTreeNode> children()
	{
		if(children == null)
		{
			children = new CList();
			
			loadChildren();
		}
		return children;
	}
	
	
	protected final void forceLoadChildren()
	{
		children = null;
	}
	
	
	protected void clearChildren()
	{
		children = new CList();
	}
	
	
	public boolean isLeaf()
	{
		return children().size() == 0;
	}
	
	
	public int getChildrenCount()
	{
		return children().size();
	}

	
	public void addChild(int index, CTreeNode ch)
	{
		if(index < 0)
		{
			children().add(ch);
		}
		else 
		{
			children().add(index, ch);
		}
		ch.setParent(this);
	}
	
	
	public void addChild(CTreeNode ch)
	{
		addChild(-1, ch);
	}
	
	
	public void removeChild(CTreeNode p)
	{
		int ix = indexOfChild(p);
		if(ix >= 0)
		{
			children().remove(ix);
		}
	}
	
	
	public int indexOfChild(CTreeNode p)
	{
		CList<CTreeNode> cs = children();
		
		for(int i=0; i<cs.size(); i++)
		{
			CTreeNode ch = cs.get(i);
			if(CKit.equals(ch.getTreeNodeKey(), p.getTreeNodeKey()))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public CTreeNode getChildAt(int ix)
	{
		return children().get(ix);
	}
}
