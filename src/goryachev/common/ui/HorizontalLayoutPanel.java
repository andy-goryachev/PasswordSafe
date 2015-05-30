// Copyright (c) 2006-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.ScrollablePanel;
import info.clearthought.layout.TableLayout;
import java.awt.Component;


@Deprecated // switch to CPanel3 variant
public class HorizontalLayoutPanel
	extends ScrollablePanel
{
	private int col = -1;
	
	
	public HorizontalLayoutPanel(int gap, double height)
	{
		super(new TableLayout
		(
			new double[] 
			{
				TableLayout.PREFERRED 
			}, 
			new double[] 
			{
				height 
			}
		));
		setGap(gap);
	}
	
	
	public HorizontalLayoutPanel(int gap)
	{
		this(gap, TableLayout.FILL);
	}
	
	
	public HorizontalLayoutPanel()
	{
		this(0);
	}
	
	
	public void space(int sz)
	{
		insertCol(sz);
	}
	
	
	public void space()
	{
		space(10);
	}


	public Component add(double width, Component c)
	{
		insertCol(width);
		add(c, col + ",0");
		return c;
	}


	public Component add(Component c)
	{
		insertCol(TableLayout.PREFERRED);
		add(c, col + ",0");
		return c;
	}


	public void fill(Component c)
	{
		insertCol(TableLayout.FILL);
		if(c != null)
		{
			add(c,col + ",0");
		}
	}
	
	
	public void fill()
	{
		insertCol(TableLayout.FILL);
	}
	
	
	public void addMin(Component c)
	{
		insertCol(TableLayout.MINIMUM);
		add(c,col + ",0");
	}
	
	
	protected void insertCol(double type)
	{
		col++;
		if(col == 0)
		{
			((TableLayout)getLayout()).setColumn(col,type);
		}
		else
		{
			((TableLayout)getLayout()).insertColumn(col,type);
		}
	}


	public void setGap(int n)
	{
		((TableLayout)getLayout()).setHGap(n);
	}
}