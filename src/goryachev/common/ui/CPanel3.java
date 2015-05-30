// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


/** A convenient JPanel with CTableLayout3, a hybrid between BorderLayout and TableLayout. */
public class CPanel3
	extends JPanel
	implements Scrollable
{
	public static final float FILL = CTableLayout3.FILL;
	public static final float MINIMUM = CTableLayout3.MINIMUM;
	public static final float PREFERRED = CTableLayout3.PREFERRED;
	
	private boolean trackWidth = true;
	private boolean trackHeight;
	private int currentRow;
	
	
	public CPanel3()
	{
		super(new CTableLayout3());
	}
	
	
	public CPanel3(int hgap, int vgap)
	{
		super(new CTableLayout3());
		setGaps(hgap, vgap);
	}
	
	
	public CPanel3(boolean opaque)
	{
		super(new CTableLayout3());
		setOpaque(opaque);
	}
	
	
	public CPanel3(int hgap, int vgap, boolean opaque)
	{
		super(new CTableLayout3());
		setGaps(hgap, vgap);
		setOpaque(opaque);
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
		if(m instanceof CTableLayout3)
		{
			super.setLayout(m);
		}
		else
		{
			throw new Rex();
		}
	}
	
	
	private Component set(Component c, CTableLayout3.CC cc)
	{
		Component old = tableLayout().getBorderComponent(cc);
		if(old != null)
		{
			remove(old);
		}
		
		if(c != null)
		{
			add(c, cc);
		}
		return old;
	}
	

	public Component setCenter(Component c)
	{
		return set(c, CTableLayout3.CENTER);
	}


	public Component getCenter()
	{
		return tableLayout().getBorderComponent(CTableLayout3.CENTER);
	}


	public Component setEast(Component c)
	{
		return setTrailing(c);
	}
	
	
	public Component setTrailing(Component c)
	{
		return set(c, CTableLayout3.TRAILING);
	}


	public Component getTrailing()
	{
		return tableLayout().getBorderComponent(CTableLayout3.TRAILING);
	}


	public Component setWest(Component c)
	{
		return setLeading(c);
	}
	
	
	public Component setLeading(Component c)
	{
		return set(c, CTableLayout3.LEADING);
	}


	public Component getLeading()
	{
		return tableLayout().getBorderComponent(CTableLayout3.LEADING);
	}


	public Component setNorth(Component c)
	{
		return setTop(c);
	}


	public Component setTop(Component c)
	{
		return set(c, CTableLayout3.TOP);
	}


	public Component getTop()
	{
		return tableLayout().getBorderComponent(CTableLayout3.TOP);
	}


	public Component setSouth(Component c)
	{
		return setBottom(c);
	}
	
	
	public Component setBottom(Component c)
	{
		return set(c, CTableLayout3.BOTTOM);
	}


	public Component getBottom()
	{
		return tableLayout().getBorderComponent(CTableLayout3.BOTTOM);
	}


	public Dimension getPreferredScrollableViewportSize()
	{
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


	public void setPreferredSize(int w, int h)
	{
		setPreferredSize(new Dimension(w, h));
	}


	public void setMinimumSize(int w, int h)
	{
		setMinimumSize(new Dimension(w, h));
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
	
	
	public CTableLayout3 tableLayout()
	{
		return (CTableLayout3)getLayout();
	}
	
	
	public int getTableLayoutRowCount()
	{
		return tableLayout().getRowCount();
	}
	
	
	public int getTableLayoutColumnCount()
	{
		return tableLayout().getColumnCount();
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
		tableLayout().setHGap(gap);
	}


	public void setVGap(int gap)
	{
		tableLayout().setVGap(gap);
	}


	public int getHGap()
	{
		return tableLayout().getHGap();
	}


	public int getVGap()
	{
		return tableLayout().getVGap();
	}
	
	
	public void setColumnMinimumSize(int col, int size)
	{
		tableLayout().setColumnMinimumSize(col, size);
	}
	
	
	public void addRow()
	{
		addRow(PREFERRED);
	}
	
	
	public void addFillRow()
	{
		addRow(FILL);
	}
	
	
	public void addRow(float spec)
	{
		tableLayout().addRow(spec);
	}
	
	
	public void addRows(float ... specs)
	{
		for(float rs: specs)
		{
			addRow(rs);
		}
	}
	
	
	public void nextRow()
	{
		nextRow(PREFERRED);
	}
	
	
	public void nextFillRow()
	{
		nextRow(FILL);
	}
	
	
	public void nextRow(float spec)
	{
		addRow(spec);
		currentRow++;
	}
	
	
	public void addColumn(float spec)
	{
		tableLayout().addColumn(spec);
	}
	
	
	public void addColumn()
	{
		addColumn(PREFERRED);
	}
	
	
	public void addColumns(float ... specs)
	{
		for(float cs: specs)
		{
			addColumn(cs);
		}
	}
	
	
	protected int row()
	{
		while(currentRow >= getTableLayoutRowCount())
		{
			addRow();
		}
		return currentRow;
	}
	
	
	public void row(int col, Component c)
	{
		int r = row();
		add(c, new CTableLayout3.CC(col, r));
	}
	
	
	public void row(int col, int colSpan, Component c)
	{
		int r = row();
		add(c, new CTableLayout3.CC(col, r, col + colSpan - 1, r));
	}
	
	
	public void row(int col, int colSpan, int rowSpan, Component c)
	{
		int r = row();
		add(c, new CTableLayout3.CC(col, r, col + colSpan - 1, r + rowSpan - 1));
	}
	
	
	public void add(int col, int row, Component c)
	{
		add(col, row, 1, 1, c);
	}
	
	
	public void add(int col, int row, int colSpan, int rowSpan, Component c)
	{
		add(c, new CTableLayout3.CC(col, row, col + colSpan - 1, row + rowSpan - 1));
	}
	
	
	public CButtonPanel3 buttonPanel()
	{
		Component c = getBottom();
		if(c instanceof CButtonPanel3)
		{
			return (CButtonPanel3)c;
		}
		else
		{
			CButtonPanel3 p = new CButtonPanel3();
			setBottom(p);
			UI.validateAndRepaint(this);
			return p;
		}
	}
	
	
	public boolean hasButtonPanel()
	{
		Component c = getBottom();
		return (c instanceof CButtonPanel3);
	}
}

