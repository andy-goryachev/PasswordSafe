// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.ColorTools;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class CScrollBarUI
	extends BasicScrollBarUI
{
	private static final Border BORDER = new CBorder.UIResource();
	private static int barWidth = 16;
	private static int serrationMargin = 4;
	private static int hoverAlpha = 128;
	//private static int trackAlpha = 8;
	
	
	public CScrollBarUI()
	{
	}


	public static void init(UIDefaults defs)
	{
		defs.put("ScrollBarUI", CScrollBarUI.class.getName());
		defs.put("ScrollBar.minimumThumbSize", new DimensionUIResource(10,10));
		defs.put("ScrollBar.maximumThumbSize", new DimensionUIResource(4096,4096));
		defs.put("ScrollBar.border", BORDER);
		defs.put("ScrollBar.track", new ColorUIResource(Theme.fieldBG()));
	}
	

	protected void installDefaults()
	{
		super.installDefaults();
		
//		minimumThumbSize = new Dimension(8,8);
//		scrollbar.setBorder(null);
		
//		trackColor = Theme.panelBG();
//		trackColor = Theme.fieldBG();
	}


	public void uninstallUI(JComponent c)
	{
		super.uninstallUI(c);
	}

	
	public JScrollBar getScrollBar()
	{
		return scrollbar;
	}


	protected ArrowButtonListener createArrowButtonListener()
	{
		return new ArrowButtonListener()
		{
			public void mouseEntered(MouseEvent evt)
			{
				repaint();
				super.mouseEntered(evt);
			}


			public void mouseExited(MouseEvent evt)
			{
				repaint();
				super.mouseExited(evt);
			}


			private void repaint()
			{
				getScrollBar().repaint();
			}
		};

//		return super.createArrowButtonListener();
	}


	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
	{
		boolean vertical = (scrollbar.getOrientation() == JScrollBar.VERTICAL);
		
		// looks bad
		//GradientPainter.paint(g, !vertical, trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, trackColor, 18, 18, Theme.getGradientFactor(), true, false);
		
		// looks too dark
		// Color bg = CKit.setAlpha(Theme.textFG(), trackAlpha);
		Color bg = trackColor;
		g.setColor(bg);
		g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);

		if(trackHighlight == DECREASE_HIGHLIGHT)
		{
			paintDecreaseHighlight(g);
		}
		else if(trackHighlight == INCREASE_HIGHLIGHT)
		{
			paintIncreaseHighlight(g);
		}
	}


	protected void paintThumb(Graphics g, JComponent c, Rectangle th)
	{
		if(th.isEmpty() || !scrollbar.isEnabled())
		{
			return;
		}

		g.translate(th.x, th.y);

		boolean vertical = (scrollbar.getOrientation() == JScrollBar.VERTICAL);
		GradientPainter.paint(g, !vertical, th.width, th.height, thumbColor, 50, 50, Theme.getGradientFactor(), false, true); 

		// serration
		if(Math.min(th.height,th.width) > 13)
		{
			g.setColor(Theme.panelBG().darker());
			if(vertical)
			{
				int x0 = serrationMargin;
				int y0 = th.height/2;
				int x2 = th.width - serrationMargin;
				for(int i=-2; i<=2; i+=2)
				{
					int y = y0 + i;
					g.drawLine(x0, y, x2, y); 
				}
			}
			else
			{
				int x0 = th.width/2;
				int y0 = serrationMargin;
				int y2 = th.height - serrationMargin;
				for(int i=-2; i<=2; i+=2)
				{
					int x = x0 + i;
					g.drawLine(x,y0,x,y2);
				}
			}
		}

		// TODO hover
		
		Theme.raisedBevelBorder().paintBorder(scrollbar, g, 0, 0, th.width, th.height);

		g.translate(-th.x, -th.y);
	}


	protected void paintDecreaseHighlight(Graphics g)
	{
		Insets insets = scrollbar.getInsets();
		Rectangle thumbR = getThumbBounds();
		int x, y, w, h;

		if(scrollbar.getOrientation() == JScrollBar.VERTICAL)
		{
			x = insets.left;
			y = decrButton.getY() + decrButton.getHeight();
			w = scrollbar.getWidth() - (insets.left + insets.right);
			h = thumbR.y - y;
		}
		else
		{
			x = decrButton.getX() + decrButton.getHeight();
			y = insets.top;
			w = thumbR.x - x;
			h = scrollbar.getHeight() - (insets.top + insets.bottom);
		}

		g.setColor(ColorTools.alpha(Theme.hoverColor(), hoverAlpha));
		g.fillRect(x, y, w, h);
	}


	protected void paintIncreaseHighlight(Graphics g)
	{
		Insets insets = scrollbar.getInsets();
		Rectangle thumbR = getThumbBounds();
		int x, y, w, h;

		if(scrollbar.getOrientation() == JScrollBar.VERTICAL)
		{
			x = insets.left;
			y = thumbR.y + thumbR.height;
			w = scrollbar.getWidth() - (insets.left + insets.right);
			h = incrButton.getY() - y;
		}
		else
		{
			x = thumbR.x + thumbR.width;
			y = insets.top;
			w = incrButton.getX() - x;
			h = scrollbar.getHeight() - (insets.top + insets.bottom);
		}
		
		g.setColor(ColorTools.alpha(Theme.hoverColor(), hoverAlpha));
		g.fillRect(x,y,w,h);
	}


	protected void setThumbRollover(boolean active)
	{
		//boolean old = isThumbRollover();
		super.setThumbRollover(active);
		
		// TODO
		// we need to repaint the entire scrollbar because state change for thumb
		// causes state change for incr and decr buttons on Vista
//		if(XPStyle.isVista() && active != old)
		{
			scrollbar.repaint();
		}
	}
	
	
    public Dimension getPreferredSize(JComponent c)
	{
		return (scrollbar.getOrientation() == JScrollBar.VERTICAL) ? new Dimension(barWidth, 48) : new Dimension(48, barWidth);
	}


	protected JButton createDecreaseButton(int orientation)
	{
		return new CScrollBarArrowButton(this, orientation);
	}


	protected JButton createIncreaseButton(int orientation)
	{
		return new CScrollBarArrowButton(this, orientation);
	}
	

	protected void layoutVScrollbar(JScrollBar sb)
	{
		Dimension sbSize = sb.getSize();
		Insets sbInsets = sb.getInsets();

		// Width and left edge of the buttons and thumb.
		int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
		int itemX = sbInsets.left;

		boolean squareButtons = false; //DefaultLookup.getBoolean(scrollbar, this, "ScrollBar.squareButtons", false);
		int decrButtonH = squareButtons ? itemW : decrButton.getPreferredSize().height;
		int decrButtonY = sbInsets.top;

		int incrButtonH = squareButtons ? itemW : incrButton.getPreferredSize().height;
		int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);

		// The thumb must fit within the height left over after we
		// subtract the preferredSize of the buttons and the insets and the gaps
		int sbInsetsH = sbInsets.top + sbInsets.bottom;
		int sbButtonsH = decrButtonH + incrButtonH;
		float trackH = sbSize.height - (sbInsetsH + sbButtonsH);

		// Compute the height and origin of the thumb.   The case
		// where the thumb is at the bottom edge is handled specially 
		// to avoid numerical problems in computing thumbY.  Enforce
		// the thumbs min/max dimensions.  If the thumb doesn't
		// fit in the track (trackH) we'll hide it later.
		float min = sb.getMinimum();
		float extent = sb.getVisibleAmount();
		float range = sb.getMaximum() - min;
		float value = sb.getValue();

		int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int) (trackH * (extent / range));
		thumbH = Math.max(thumbH, getMinimumThumbSize().height);
		thumbH = Math.min(thumbH, getMaximumThumbSize().height);

		int thumbY = incrButtonY - thumbH;
		if(value < (sb.getMaximum() - sb.getVisibleAmount()))
		{
			float thumbRange = trackH - thumbH;
			thumbY = (int) (0.5f + (thumbRange * ((value - min) / (range - extent))));
			thumbY += decrButtonY + decrButtonH;
		}

		// If the buttons don't fit, allocate half of the available 
		// space to each and move the lower one (incrButton) down.
		int sbAvailButtonH = (sbSize.height - sbInsetsH);
		if(sbAvailButtonH < sbButtonsH)
		{
			incrButtonH = decrButtonH = sbAvailButtonH / 2;
			incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
		}
		decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
		incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);

		// Update the trackRect field.
		int itrackY = decrButtonY + decrButtonH;
		int itrackH = incrButtonY - itrackY;
		trackRect.setBounds(itemX, itrackY, itemW, itrackH);

		// If the thumb isn't going to fit, zero it's bounds.  Otherwise
		// make sure it fits between the buttons.  Note that setting the
		// thumbs bounds will cause a repaint.
		if(thumbH >= (int) trackH)
		{
			setThumbBounds(0, 0, 0, 0);
		}
		else
		{
			if((thumbY + thumbH) > incrButtonY)
			{
				thumbY = incrButtonY - thumbH;
			}
			if(thumbY < (decrButtonY + decrButtonH))
			{
				thumbY = decrButtonY + decrButtonH + 1;
			}
			setThumbBounds(itemX, thumbY, itemW, thumbH);
		}
	}
}
