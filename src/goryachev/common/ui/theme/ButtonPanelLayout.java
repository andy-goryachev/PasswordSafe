// Copyright (c) 2012-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


public class ButtonPanelLayout
    implements LayoutManager2
{
	private int gap = 10;
	private int minButtonWidth = 70;
	
	
	public ButtonPanelLayout()
	{
	}

	
	public int getGap()
	{
		return gap;
	}


	public void setGap(int vgap)
	{
		this.gap = vgap;
	}


	public float getLayoutAlignmentX(Container parent)
	{
		return 0.5f;
	}


	public float getLayoutAlignmentY(Container parent)
	{
		return 0.5f;
	}


	public void invalidateLayout(Container target)
	{
	}


	public void addLayoutComponent(Component c, Object constraints)
	{
	}


	public void addLayoutComponent(String name, Component c)
	{
	}


	public void removeLayoutComponent(Component c)
	{
	}
	
	
	protected Component[] components(Container c)
	{
		return c.getComponents();
	}
	

	public Dimension minimumLayoutSize(Container target)
	{
		synchronized(target.getTreeLock())
		{
			Dimension rv = new Dimension(0, 0);

			boolean add = false;
			for(Component c: components(target))
			{
				if(add)
				{
					rv.width += gap;
				}
				else
				{
					add = true;
				}
				
				Dimension d = c.getMinimumSize();
				rv.height = Math.max(d.height, rv.height);
				rv.width += d.width;
			}

			Insets insets = target.getInsets();
			rv.width += (insets.left + insets.right);
			rv.height += (insets.top + insets.bottom);

			return rv;
		}
	}


	public Dimension preferredLayoutSize(Container target)
	{
		synchronized(target.getTreeLock())
		{
			Dimension rv = new Dimension(0, 0);
			
			boolean add = false;
			for(Component c: components(target))
			{
				if(add)
				{
					rv.width += gap;
				}
				else
				{
					add = true;
				}
				
				Dimension d = c.getPreferredSize();
				rv.height = Math.max(d.height, rv.height);
				rv.width += Math.min(minButtonWidth, d.width);
			}

			Insets insets = target.getInsets();
			rv.width += insets.left + insets.right;
			rv.height += insets.top + insets.bottom;

			return rv;
		}
	}


	public Dimension maximumLayoutSize(Container target)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}


	public void layoutContainer(Container target)
	{
		synchronized(target.getTreeLock())
		{
			Insets insets = target.getInsets();
			int top = insets.top;
			int bottom = target.getHeight() - insets.bottom;
			int left = insets.left;
			int right = target.getWidth() - insets.right;
			int sz = target.getComponentCount() - 1;
			int h = bottom - top;
			boolean ltr = target.getComponentOrientation().isLeftToRight();
			
			// step 1: lay out components to the left
			if(ltr)
			{
				boolean addGap = false;
				int x = left;
				for(Component c: target.getComponents())
				{
					if(addGap)
					{
						x += gap;
					}
					else
					{
						addGap = true;
					}
					
					Dimension d = c.getPreferredSize();
					int w = Math.max(minButtonWidth, d.width);
					c.setSize(w, h);
					c.setBounds(x, top, w, h);
					
					x += w;
				}
				
				// step 2. check if all fits
				int dx = right - x;
				// move everything dx pixels to the right
				for(int i=sz; i>=0; --i)
				{
					Component c = target.getComponent(i);
					c.setLocation(c.getX() + dx, c.getY());
				}
			}
			else
			{
				// lay out components right to left
				boolean addGap = false;
				int x = right;
				for(Component c: target.getComponents())
				{
					if(addGap)
					{
						x -= gap;
					}
					else
					{
						addGap = true;
					}
					
					Dimension d = c.getPreferredSize();
					int w = Math.max(minButtonWidth, d.width);
					
					x -= w;

					c.setSize(w, h);
					c.setBounds(x, top, w, h);
				}
				
				// step 2. check if all fits
				int dx = x - left;
				// move everything dx pixels to the left
				for(int i=sz; i>=0; --i)
				{
					Component c = target.getComponent(i);
					c.setLocation(c.getX() - dx, c.getY());
				}
			}
		}
	}
}
