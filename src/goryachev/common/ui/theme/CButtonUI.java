// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CSkin;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;


public class CButtonUI
	extends BasicButtonUI
{
	protected int dashedRectGapX;
	protected int dashedRectGapY;
	protected int dashedRectGapWidth;
	protected int dashedRectGapHeight;
	private boolean defaults_initialized;
	private final static CButtonUI ui = new CButtonUI();
	private static boolean isMnemonicHidden;
	private static Insets margin = UI.newInsets(2,10,2,10);
	// TODO paint button gradient on top of the skin?
	private static CSkin SKIN = new CButtonSkin();
	private static CButtonUiBorder BORDER = new CButtonUiBorder();
	

	public static void init(UIDefaults defs)
	{
		defs.put("Button.showMnemonics", Boolean.TRUE);
		
		defs.put("Button.shadow", UI.mix(127, Theme.textFG(), Theme.textBG()));
		defs.put("Button.disabledShadow", new Color(255, 255, 255, 224));
	}
	
	
	public static ComponentUI createUI(JComponent c)
	{
		return ui;
	}


	protected void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		
		if(!defaults_initialized)
		{
			dashedRectGapX = 5;
			dashedRectGapY = 4;
			dashedRectGapWidth = 10;
			dashedRectGapHeight = 8;
			defaults_initialized = true;
		}
		
		CSkin.set(b, SKIN);
		
		if(UI.isNullOrResource(b.getBorder()))
		{
			b.setBorder(createBorder());
		}
		
		if(UI.isNullOrResource(b.getMargin()))
		{
			b.setMargin(createMargin());
		}
	}
	
	
	protected Insets createMargin()
	{
		return margin;
	}
	
	
	protected Border createBorder()
	{
		// TODO background shows up of corners are not painted
		// FIX paint() insets
		return BORDER;
	}


	protected void uninstallDefaults(AbstractButton b)
	{
		super.uninstallDefaults(b);
		defaults_initialized = false;
	}


	protected Color getFocusColor()
	{
		return Theme.focusColor();
	}


	protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text)
	{
		ThemeTools.paintText(g, b, textRect, text, getTextShiftOffset());
	}


	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect)
	{
		int width = b.getWidth();
		int height = b.getHeight();
		g.setColor(getFocusColor());
		BasicGraphicsUtils.drawDashedRect
		(
			g, 
			dashedRectGapX + getTextShiftOffset(), 
			dashedRectGapY + getTextShiftOffset(), 
			width - dashedRectGapWidth, 
			height - dashedRectGapHeight
		);
	}


	public Dimension getPreferredSize(JComponent c)
	{
		Dimension d = super.getPreferredSize(c);

		// Ensure that the width and height of the button is odd,
		// to allow for the focus line if focus is painted
		AbstractButton b = (AbstractButton) c;
		if(d != null && b.isFocusPainted())
		{
			if(d.width % 2 == 0)
			{
				d.width += 1;
			}
			if(d.height % 2 == 0)
			{
				d.height += 1;
			}
		}
		return d;
	}


	private static Insets getOpaqueInsets(Border b, Component c)
	{
		if(b == null)
		{
			return null;
		}
		if(b.isBorderOpaque())
		{
			return b.getBorderInsets(c);
		}
		else if(b instanceof CompoundBorder)
		{
			CompoundBorder cb = (CompoundBorder) b;
			Insets iOut = getOpaqueInsets(cb.getOutsideBorder(), c);
			if(iOut != null && iOut.equals(cb.getOutsideBorder().getBorderInsets(c)))
			{
				// Outside border is opaque, keep looking
				Insets iIn = getOpaqueInsets(cb.getInsideBorder(), c);
				if(iIn == null)
				{
					// Inside is non-opaque, use outside insets
					return iOut;
				}
				else
				{
					// Found non-opaque somewhere in the inside (which is
					// also compound).
					return new Insets(iOut.top + iIn.top, iOut.left + iIn.left, iOut.bottom + iIn.bottom, iOut.right + iIn.right);
				}
			}
			else
			{
				// Outside is either all non-opaque or has non-opaque
				// border inside another compound border
				return iOut;
			}
		}
		else
		{
			return null;
		}
	}
	
	
	public boolean isButtonSelected(JComponent c)
	{
		if(c instanceof AbstractButton)
		{
			return ((AbstractButton)c).isSelected();
		}
		return false;
	}


	public void paint(Graphics g, JComponent c)
	{
		setBorderPressed(c, false);
		
		if(isButtonSelected(c))
		{
			g.setColor(Theme.brighter(Theme.panelBG()));
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		}
		else
		{
			// TODO paint gradient shifted by one pixel if pressed!
			CSkin skin = CSkin.get(c);
			if(skin != null)
			{
				skin.paint(g, c);
			}
		}
		
		super.paint(g, c);
	}
	

	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
		setBorderPressed(b, true);
		setTextShiftOffset();
	}
	
	
	protected void setBorderPressed(JComponent c, boolean on)
	{
		Border br = c.getBorder();
		if(br instanceof CButtonBorder)
		{
			((CButtonBorder)br).setPressed(on);
		}
	}
	
	
	//
	
	
	public static class CButtonUiBorder
		extends CButtonBorder
		implements UIResource
	{
		public CButtonUiBorder()
		{ }
	}
}
