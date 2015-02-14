// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


// border layout panel where center component can be configured to have table layout
public class CPanel
	extends JPanel
	implements Scrollable
{
	protected void onAddNotify() { }
	
	//
	
	public static final double PREFERRED = TableLayout.PREFERRED;
	public static final double FILL = TableLayout.FILL; 
	public static final double MINIMUM = TableLayout.MINIMUM;
	
	private boolean trackWidth = true;
	private boolean trackHeight;
	
	
	public CPanel()
	{
		super(new CBorderLayout());
	}
	
	
	public CPanel(int hgap, int vgap)
	{
		super(new CBorderLayout(hgap,vgap));
	}
	
	
	/** creates standard 10-pixel border */
	public void border()
	{
		setBorder(Theme.BORDER_10);
	}
	
	
	public void borderNoBottomGap()
	{
		setBorder(new CBorder(10, 10, 0, 10));
	}
	
	
	public void setLayout(LayoutManager m)
	{
		if(m instanceof CBorderLayout)
		{
			super.setLayout(m);
		}
		else
		{
			throw new RuntimeException();
		}
	}
	
	
	private Component set(Component c, String constraint)
	{
		Component old = getBorderLayout().getLayoutComponent(constraint);
		if(old != null)
		{
			remove(old);
		}
		
		if(c != null)
		{
			add(c, constraint);
		}
		return old;
	}
	

	protected CBorderLayout getBorderLayout()
	{
		return (CBorderLayout)getLayout();
	}


	public Component setCenter(Component c)
	{
		return set(c, BorderLayout.CENTER);
	}


	public Component getCenter()
	{
		return getBorderLayout().getLayoutComponent(BorderLayout.CENTER);
	}


	public Component setEast(Component c)
	{
		return set(c, BorderLayout.EAST);
	}


	public Component getEast()
	{
		return getBorderLayout().getLayoutComponent(BorderLayout.EAST);
	}


	public Component setWest(Component c)
	{
		return set(c, BorderLayout.WEST);
	}


	public Component getWest()
	{
		return getBorderLayout().getLayoutComponent(BorderLayout.WEST);
	}


	public Component setNorth(Component c)
	{
		return set(c, BorderLayout.NORTH);
	}


	public Component getNorth()
	{
		return getBorderLayout().getLayoutComponent(BorderLayout.NORTH);
	}


	public Component setSouth(Component c)
	{
		return set(c, BorderLayout.SOUTH);
	}


	public Component getSouth()
	{
		return getBorderLayout().getLayoutComponent(BorderLayout.SOUTH);
	}


	public Dimension getPreferredScrollableViewportSize()
	{
		//return getPreferredSize();
		return null;
	}


	public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction)
	{
		return 10;
	}
	

	public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction)
	{
		switch(orientation)
		{
		case SwingConstants.HORIZONTAL: return (r.width * 80 / 100);
		case SwingConstants.VERTICAL:   return (r.height * 80 / 100);
		}
		return 10;
	}
	

	public boolean getScrollableTracksViewportWidth()
	{
		return trackWidth;
	}
	
	
	public void setScrollableTracksViewportWidth(boolean on)
	{
		trackWidth = on;
	}
	

	public boolean getScrollableTracksViewportHeight()
	{
		return trackHeight;
	}
	
	
	public void setScrollableTracksViewportHeight(boolean on)
	{
		trackHeight = on;
	}


	public void setLayout(double[] cols, double[] rows)
	{
		setLayout(cols, rows, 0, 0);
	}


	public void setLayout(double[] cols, double[] rows, int hgap, int vgap)
	{
		TableLayout layout = new TableLayout(cols, rows);
		layout.setHGap(hgap);
		layout.setVGap(vgap);
		JPanel p = new JPanel(layout);
		p.setOpaque(false);
		setCenter(p);
	}


	public TableLayout getTableLayout()
	{
		Component c = getCenter();
		if(c instanceof JComponent)
		{
			LayoutManager m = ((JComponent)c).getLayout();
			if(m instanceof TableLayout)
			{
				return (TableLayout)m;
			}
		}
		return null;
	}


	public void deleteRow(int row)
	{
		getTableLayout().deleteRow(row);
	}


	public int getTableLayoutRowCount()
	{
		TableLayout la = getTableLayout();
		if(la != null)
		{
			return la.getNumRow();
		}
		return 0;
	}


	public int getColumnCount()
	{
		TableLayout la = getTableLayout();
		if(la != null)
		{
			return la.getNumColumn();
		}
		return 0;
	}


	public void addRow(double spec)
	{
		TableLayout la = getTableLayout();
		int ix = getTableLayoutRowCount();
		la.insertRow(ix, spec);
	}


	public void add(int col, int row, Component c)
	{
		((Container)getCenter()).add(c, new TableLayoutConstraints(col, row));
	}


	public void add(int col, int row, String text)
	{
		add(col, row, label(text));
	}


	public void add(int startCol, int startRow, int endCol, int endRow, Component c)
	{
		((Container)getCenter()).add(c, new TableLayoutConstraints(startCol, startRow, endCol, endRow));
	}


	public void setPreferredSize(int w, int h)
	{
		setPreferredSize(new Dimension(w, h));
	}


	public void setMinimumSize(int w, int h)
	{
		setMinimumSize(new Dimension(w, h));
	}


	public void setGaps(int gap)
	{
		setGaps(gap, gap);
	}


	public void setGaps(int horizontal, int vertical)
	{
		setHGap(horizontal);
		setVGap(vertical);
	}


	public void setHGap(int gap)
	{
		getBorderLayout().setHgap(gap);
	}


	public void setVGap(int gap)
	{
		getBorderLayout().setVgap(gap);
	}


	public int getHGap()
	{
		return getBorderLayout().getHgap();
	}


	public int getVGap()
	{
		return getBorderLayout().getVgap();
	}


	public void addNotify()
	{
		super.addNotify();
		onAddNotify();
	}


	public JLabel label(String s)
	{
		JLabel t = new JLabel(s);
		t.setHorizontalAlignment(JLabel.TRAILING);
		return t;
	}


	public JLabel labelTopAligned(String s)
	{
		JLabel t = label(s);
		t.setVerticalAlignment(JLabel.TOP);
		return t;
	}
	
	
	public JLabel labelBottomAligned(String s)
	{
		JLabel t = label(s);
		t.setVerticalAlignment(JLabel.BOTTOM);
		return t;
	}


	public JLabel heading(String s)
	{
		JLabel t = new JLabel(s);
		t.setFont(t.getFont().deriveFont(Font.BOLD));
		return t;
	}


	public InfoField info(String text)
	{
		InfoField t = new InfoField(text);
		return t;
	}
	
	
	public InfoField text(String text)
	{
		InfoField t = new InfoField(text);
		t.setForeground(Theme.textFG());
		return t;
	}
	
	
	public void setBorder()
	{
		setBorder(Theme.BORDER_10);
	}
	
	
	public void setBorder(int gap)
	{
		setBorder(new CBorder(gap));
	}
	
	
	public void setBorder(int vertGap, int horGap)
	{
		setBorder(new CBorder(vertGap, horGap));
	}
	
	
	// TODO
//	public void removeRow(int row)
//	{
//		TableLayout t = getTableLayout();
//		if(t != null)
//		{
//			t.get
//		}
//	}
	
	
	//
	
	
	private static class CBorderLayout extends BorderLayout
	{
		public CBorderLayout() { }
		public CBorderLayout(int hgap, int vgap) { super(hgap,vgap); }
	}
}
