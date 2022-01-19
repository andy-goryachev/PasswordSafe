// Copyright © 2018-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


/**
 * A layout manager that arranges components in a grid of equal size cells.
 * The cell size is determined by the largest preferred size.
 */
public class CFlowLayout
	implements LayoutManager2
{
	protected static final Log log = Log.get("CFlowLayout");
	protected int hgap;
	protected int vgap;
	protected int top;
	protected int left;
	protected int bottom;
	protected int right;
	
	
	public CFlowLayout(int hgap, int vgap, int top, int left, int bottom, int right)
	{
		this.hgap = hgap;
		this.vgap = vgap;
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
	}
	
	
	public CFlowLayout(int hgap, int vgap, int topBottom, int leftRight)
	{
		this(hgap, vgap, topBottom, leftRight, topBottom, leftRight);
	}
	
	
	public CFlowLayout(int hgap, int vgap, int padding)
	{
		this(hgap, vgap, padding, padding, padding, padding);
	}
	
	
	public CFlowLayout(int gap, int padding)
	{
		this(gap, gap, padding, padding, padding, padding);
	}
	
	
	public CFlowLayout()
	{
		this(0, 0, 0, 0, 0, 0);
	}
	

	public void addLayoutComponent(String name, Component comp)
	{
	}
	

	public void addLayoutComponent(Component comp, Object constraints)
	{
	}


	public void removeLayoutComponent(Component comp)
	{
	}



	public float getLayoutAlignmentX(Container target)
	{
		return 0.5f;
	}


	public float getLayoutAlignmentY(Container target)
	{
		return 0.5f;
	}


	public void invalidateLayout(Container target)
	{
	}


	public Dimension maximumLayoutSize(Container target)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}


	public Dimension minimumLayoutSize(Container parent)
	{
		return preferredLayoutSize(parent);
	}
	

	public Dimension preferredLayoutSize(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			Helper h = new Helper(parent);
			return h.computePreferredSize();
		}
	}

	
	public void layoutContainer(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			try
			{
				Helper h = new Helper(parent);
				h.layout();
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
	}
	

	//
	
	
	public class Helper
	{
		public final Container parent;
		private final boolean ltr; // TODO
		public int mtop;
		public int mbottom;
		public int mleft;
		public int mright;
		private int cellWidth;
		private int cellHeight;
		private boolean degenerate;
		private int columns;
		private int rows;


		public Helper(Container parent)
		{
			this.parent = parent;
			ltr = parent.getComponentOrientation().isLeftToRight();
				
			Insets m = parent.getInsets();
			mtop = m.top;
			mbottom = m.bottom;
			mleft = m.left;
			mright = m.right;
		}
		
		
		public void computeCellSize()
		{
			int w = 0;
			int h = 0;
			
			int count = parent.getComponentCount();
			for(int i=0; i<count; i++)
			{
				Component c = parent.getComponent(i);
				if(c.isVisible())
				{
					Dimension d = c.getMinimumSize();
					if(w < d.width)
					{
						w = d.width;
					}
					if(h < d.height)
					{
						h = d.height;
					}
					
					d = c.getPreferredSize();
					if(w < d.width)
					{
						w = d.width;
					}
					if(h < d.height)
					{
						h = d.height;
					}
				}
			}
			
			degenerate = ((w == 0) || (h == 0));
			cellWidth = w;
			cellHeight = h;
			
			if(degenerate)
			{
				columns = 0;
				rows = 0;
			}
			else
			{
				int width = parent.getWidth() - mleft - left - mright - right;
				columns = (width + hgap) / (cellWidth + hgap);
				rows = CKit.binCount(parent.getComponentCount(), columns);
			}
		}
		
		
		public Dimension computePreferredSize()
		{
			computeCellSize();
			
			// TODO optimize
			if(degenerate)
			{
				return new Dimension(mleft + mright + left + right, mtop + mbottom + top + bottom);
			}
						
			int w = mleft + mright + left + right + (cellWidth * columns) + (hgap * (columns - 1));
			int h = mtop + mbottom + top + bottom + (cellHeight * rows) + (vgap * (rows - 1));
			return new Dimension(w, h);
		}
		
		
		public void layout()
		{
			computeCellSize();
			
			int x0 = mleft + left;
			int y = mtop + top;
			
			if(degenerate)
			{
				int count = parent.getComponentCount();
				for(int i=0; i<count; i++)
				{
					Component c = parent.getComponent(i);
					c.setBounds(x0, y, 0, 0);
				}
			}
			else
			{
				int col = 0;
				int x = x0;
				int count = parent.getComponentCount();
				for(int i=0; i<count; i++)
				{
					if(col >= columns)
					{
						col = 0;
						y += (cellHeight + vgap);
						x = x0;
					}
					
					Component c = parent.getComponent(i);
					if(c.isVisible())
					{
						c.setBounds(x, y, cellWidth, cellHeight);
						x += (cellWidth + hgap);
						col++;
					}
					else
					{
						c.setBounds(x, y, 0, 0);
					}
				}
			}
		}
	}
}
