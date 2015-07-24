// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;


public class ThemeTools
{
	public static final ThemeColor BRIGHTER = ThemeColor.highlight(Theme.PANEL_BG, 0.15);
	public static final ThemeColor DARKER = ThemeColor.shadow(Theme.PANEL_BG, 0.15);
	public static final ThemeColor DARKER_DARKER = ThemeColor.shadow(Theme.PANEL_BG, 0.3);

	private static final int CHAR_BUFFER_SIZE = 128;
	private static final Object charsBufferLock = new Object();
	private static char[] charsBuffer = new char[CHAR_BUFFER_SIZE];


	public static FontMetrics getFontMetrics(JComponent c, Graphics g)
	{
		return getFontMetrics(c, g, g.getFont());
	}


	public static FontMetrics getFontMetrics(JComponent c, Graphics g, Font font)
	{
		if(c != null)
		{
			return c.getFontMetrics(font);
		}
		return Toolkit.getDefaultToolkit().getFontMetrics(font);
	}


	public static int stringWidth(JComponent c, FontMetrics fm, String string)
	{
		if(string == null || string.equals(""))
		{
			return 0;
		}

		return fm.stringWidth(string);
	}


	public static void drawString(JComponent c, Graphics g, String text, int x, int y)
	{
		if(text == null || text.length() <= 0)
		{
			return;
		}

		g.drawString(text, x, y);
	}


	public static void drawStringUnderlineCharAt(JComponent c, Graphics g, String text, int underlinedIndex, int x, int y)
	{
		if(text == null || text.length() <= 0)
		{
			return;
		}

		drawString(c, g, text, x, y);
		int textLength = text.length();
		if(underlinedIndex >= 0 && underlinedIndex < textLength)
		{
			int underlineRectY = y;
			int underlineRectHeight = 1;
			int underlineRectX = 0;
			int underlineRectWidth = 0;

			synchronized(charsBufferLock)
			{
				if(charsBuffer == null || charsBuffer.length < textLength)
				{
					charsBuffer = text.toCharArray();
				}
				else
				{
					text.getChars(0, textLength, charsBuffer, 0);
				}
			}

			FontMetrics fm = g.getFontMetrics();
			underlineRectX = x + stringWidth(c, fm, text.substring(0, underlinedIndex));
			underlineRectWidth = fm.charWidth(text.charAt(underlinedIndex));

			g.fillRect(underlineRectX, underlineRectY + 1, underlineRectWidth, underlineRectHeight);
		}
	}


	public static void adjustFocus(JComponent c)
	{
		if(!c.hasFocus() && c.isRequestFocusEnabled())
		{
			c.requestFocus();
		}
	}


	public static boolean shouldIgnore(MouseEvent ev, JComponent c)
	{
		return c == null || !c.isEnabled() || !SwingUtilities.isLeftMouseButton(ev) || ev.isConsumed();
	}


	public static Component compositeRequestFocus(Component c)
	{
		if(c instanceof Container)
		{
			Container container = (Container)c;
			if(container.isFocusCycleRoot())
			{
				FocusTraversalPolicy policy = container.getFocusTraversalPolicy();
				Component comp = policy.getDefaultComponent(container);
				if(comp != null)
				{
					comp.requestFocus();
					return comp;
				}
			}
			
			Container rootAncestor = container.getFocusCycleRootAncestor();
			if(rootAncestor != null)
			{
				FocusTraversalPolicy policy = rootAncestor.getFocusTraversalPolicy();
				Component comp = policy.getComponentAfter(rootAncestor, container);
				if(comp != null && SwingUtilities.isDescendingFrom(comp, container))
				{
					comp.requestFocus();
					return comp;
				}
			}
		}
		
		if(c.isFocusable())
		{
			c.requestFocus();
			return c;
		}
		
		return null;
	}


	public static boolean pointOutsidePrefSize(JTable t, int row, int column, Point p)
	{
		if(t.convertColumnIndexToModel(column) != 0 || row == -1)
		{
			return true;
		}
		
		TableCellRenderer renderer = t.getCellRenderer(row, column);
		Object value = t.getValueAt(row, column);
		Component cell = renderer.getTableCellRendererComponent(t, value, false, false, row, column);
		Dimension d = cell.getPreferredSize();
		Rectangle r = t.getCellRect(row, column, false);
		r.width = d.width;
		r.height = d.height;

		if(p.x > r.x + r.width || p.y > r.y + r.height)
		{
			return true;
		}
		return false;
	}
	
	
	public static void paintText(Graphics g, AbstractButton b, Rectangle r, String text, int shiftOffset)
	{
		FontMetrics fm = ThemeTools.getFontMetrics(b, g);
		int ix = b.getDisplayedMnemonicIndex();
		paintText(b, g, r.x + shiftOffset, r.y + fm.getAscent() + shiftOffset, text, ix);
	}


	public static void paintText(AbstractButton b, Graphics g, int x, int y, String text, int mnemonicsIndex)
	{
		ButtonModel m = b.getModel();
		Color c = b.getForeground();
		if(m.isEnabled())
		{
			if(!(b instanceof JMenuItem && m.isArmed()) && !(b instanceof JMenu && (m.isSelected() || m.isRollover())))
			{
				g.setColor(b.getForeground());
			}
			
			drawStringUnderlineCharAt(b, g, text, mnemonicsIndex, x, y);
		}
		else
		{
			if(m.isArmed())
			{
				c = AgButtonUI.DISABLED_FOREGROUND;
			}
			else
			{
				// shadow
				g.setColor(AgButtonUI.DISABLED_SHADOW);
				drawStringUnderlineCharAt(b, g, text, mnemonicsIndex, x + 1, y + 1);
				
				c = AgButtonUI.BUTTON_SHADOW;
			}

			g.setColor(c);
			drawStringUnderlineCharAt(b, g, text, mnemonicsIndex, x, y);
		}
	}
}
