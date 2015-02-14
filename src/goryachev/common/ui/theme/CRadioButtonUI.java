// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicRadioButtonUI;


public class CRadioButtonUI
	extends BasicRadioButtonUI
{
	private final static CRadioButtonUI radioButtonUI = new CRadioButtonUI();
	protected Color focusColor;
	
	
	public static void init(UIDefaults defs)
	{
		// does not work on mac
		//defs.put("RadioButtonUI", CRadioButtonUI.class.getName());
		defs.put("RadioButton.contentAreaFilled", Boolean.FALSE);
	}


	protected void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
		focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
	}


	public static ComponentUI createUI(JComponent b)
	{
		return radioButtonUI;
	}


	protected Color getFocusColor()
	{
		return focusColor;
	}


	protected void paintFocus(Graphics g, Rectangle textRect, Dimension d)
	{
		g.setColor(getFocusColor());
		BasicGraphicsUtils.drawDashedRect(g, textRect.x, textRect.y, textRect.width, textRect.height);
	}
}
