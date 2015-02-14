// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CSkin;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;


public class CComboBoxUI
	extends BasicComboBoxUI
{
	public static final Border BORDER_EDITOR = new CFieldBorder(false);
	public static final CButtonBorder BORDER_BUTTON = new CButtonBorder(false,true,false,false);
	public static final int DEFAULT_BUTTON_WIDTH = 7;
	
	protected static int arrowButtonWidth = DEFAULT_BUTTON_WIDTH;
	protected static Insets arrowButtonInsets = new Insets(1,1,1,1);
	public static CSkin SKIN = new CButtonSkin();
	public static final String IS_TABLE_CELL_EDITOR = "JComboBox.isTableCellEditor"; // should not be declared private in the base class
	private boolean inTable;
	
	
	public static void init(UIDefaults defs)
	{
		defs.put("ComboBoxUI", CComboBoxUI.class.getName());
		defs.put("ComboBox.padding", new Insets(0,0,0,0));
		defs.put("ComboBox.border", new CBorder(Theme.lineColor()));
	}
	
	
//	protected void installDefaults()
//	{
//    	// FIX 
//		LookAndFeel.installColorsAndFont(comboBox, "ComboBox.background", "ComboBox.foreground", "ComboBox.font");
//		LookAndFeel.installBorder(comboBox, "ComboBox.border");
//		LookAndFeel.installProperty(comboBox, "opaque", Boolean.TRUE);
//		Insets ins = UIManager.getInsets("ComboBox.padding");
//	}

	
	public CComboBoxUI()
	{ }
	

	protected JButton createArrowButton()
	{
		return createDefaultArrowButton();
	}
	
	
	public static JButton createDefaultArrowButton()
	{
		JButton b = new CArrowButton(CArrowButton.SOUTH)
		{
			public void paint(Graphics g)
			{
				int w = getWidth();
				int h = getHeight();
				Color origColor = g.getColor();
				boolean isPressed = getModel().isPressed();
				boolean isEnabled = isEnabled();

				SKIN.paint(g, this);
				//GradientPainter.paint(g, true, w, h, getBackground(), 50, 50, Theme.getGradientFactor(), false, false);
				//GradientPainter.paintVertical(g, 0, 0, w, h, getBackground(), 50, )

				// FIX partial button border
//				if(isPressed)
//				{
//					Theme.loweredBevelBorder().paintBorder(this, g, 0, 0, w, h);
//				}
//				else
//				{
//					Theme.raisedBevelBorder().paintBorder(this, g, 0, 0, w, h);
//				}
				
				BORDER_BUTTON.setPressed(isPressed);
				BORDER_BUTTON.paintBorder(this, g, 0, 0, w, h);

				if((h >= 5) && (w >= 5))
				{
					if(isPressed)
					{
						g.translate(1, 1);
					}
			
					// arrow
					int size = 3;
					paintTriangle(g, 1 + (w - size) / 2, (h - size) / 2, size, direction, isEnabled);
			
					if(isPressed)
					{
						g.translate(-1, -1);
					}
				}
				g.setColor(origColor);
			}
		};
		b.setName("ComboBox.arrowButton");
		b.setMargin(arrowButtonInsets);
		b.setBorder(new EmptyBorder(3,3,3,3));
		CSkin.set(b, SKIN);
		return b;
	}
	
	
	protected JComboBox comboBox()
	{
		return comboBox;
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new CComboBoxUI();
	}


	public void installUI(JComponent c)
	{
		super.installUI(c);
		
		inTable = Boolean.TRUE.equals(c.getClientProperty(IS_TABLE_CELL_EDITOR));
		comboBox.setRequestFocusEnabled(true);
	}


	public void uninstallUI(JComponent c)
	{
		super.uninstallUI(c);
	}


	protected void installListeners()
	{
		super.installListeners();
	}


	protected void uninstallListeners()
	{
		super.uninstallListeners();
	}


	protected void configureEditor()
	{
		super.configureEditor();
	}


	protected void unconfigureEditor()
	{
		super.unconfigureEditor();
	}


	public void paint(Graphics g, JComponent c)
	{
		super.paint(g, c);
	}


	public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus)
	{
		if(inTable)
		{
			bounds.x -= 1; // seems to work
		}
		else
		{
			bounds.x += 1;
		}
		bounds.y += 1;
		bounds.width -= 2;
		bounds.height -= 2;
		
		super.paintCurrentValue(g, bounds, hasFocus);
	}


	public Dimension getPreferredSize(JComponent c)
	{
		Dimension d = super.getPreferredSize(c);
		d.width += 4;
		d.height += 2;
		return d;
	}


	protected LayoutManager createLayoutManager()
	{
		return new BasicComboBoxUI.ComboBoxLayoutManager()
		{
			public void layoutContainer(Container parent)
			{
				layout(parent);
			}
		};
	}
	
	
	protected void layout(Container c)
	{
		int width = c.getWidth();
		int height = c.getHeight();

		Insets insets = getInsets();
		int buttonHeight = height - (insets.top + insets.bottom);
		int buttonWidth = arrowButtonWidth;
		
		if(arrowButton != null)
		{
			Insets arrowInsets = arrowButton.getInsets();
			buttonWidth = arrowButtonWidth + arrowInsets.left + arrowInsets.right;
		}

		if(arrowButton != null)
		{
			if(UI.isLeftToRight(c))
			{
				arrowButton.setBounds(width - (insets.right + buttonWidth), insets.top, buttonWidth, buttonHeight);
			}
			else
			{
				arrowButton.setBounds(insets.left, insets.top, buttonWidth, buttonHeight);
			}
		}
		
		if(editor != null)
		{
			Rectangle r = rectangleForCurrentValue();
			editor.setBounds(r);
		}
	}
	

	protected void installKeyboardActions()
	{
		super.installKeyboardActions();
	}
	

	protected ComboPopup createPopup()
	{
		return new CComboPopup(comboBox);
	}


	protected ComboBoxEditor createEditor()
	{
		return new CComboBoxEditor();
	}


	protected ListCellRenderer createRenderer()
	{
		BasicComboBoxRenderer r = new BasicComboBoxRenderer();
		return r;
	}
}
