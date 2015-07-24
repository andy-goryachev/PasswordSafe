// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.ui;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.ui.theme.ThemeColor;
import goryachev.common.util.CLanguage;
import goryachev.common.util.CLanguageCode;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.border.Border;


public class OnScreenKeyboard
	extends JComponent
{
	public static final int REGISTER_NORMAL = 0;
	public static final int REGISTER_UPPER  = 1;
	public static final int REGISTER_ALT    = 2;
	
	public static final Color KEY_COLOR = Theme.TEXT_BG;
	public static final Color CTRL_COLOR = ThemeColor.shadow(ThemeKey.TEXT_BG, 0.25f);
	public static final Color HOVER_COLOR = Theme.TEXT_BG;
	public static final Color BORDER_COLOR = Theme.LINE_COLOR;
	public static final Color BACKGROUND_COLOR = ThemeColor.shadow(ThemeKey.PANEL_BG, 0.2);
	public static final Border NORMAL_BORDER = new CBorder(Theme.LINE_COLOR);
	public static final Border HOVER_BORDER = new CBorder(Theme.TEXT_FG);
	
	// 0 - normal
	// 1 - shift
	// 2 - alt graph
	private int register;
	private boolean shift;
	private boolean capsLock;
	private boolean altGraph;
	

	public OnScreenKeyboard()
	{
		setBorder(new CBorder(BORDER_COLOR, 5, 5));
		setBackground(BACKGROUND_COLOR);
	}
	
	
	protected void paintComponent(Graphics g) 
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		super.paintComponent(g);
	}
	
	
	public Color getHoverColor()
	{
		return HOVER_COLOR;
	}
	
	
	public Color getNormalColor()
	{
		return KEY_COLOR;
	}
	
	
	public Color getCtrlColor()
	{
		return CTRL_COLOR;
	}
	
	
	public Border getNormalBorder()
	{
		return NORMAL_BORDER;
	}
	
	
	public Border getHoverBorder()
	{
		return HOVER_BORDER;
	}


	public int getRegister()
	{
		return register;
	}
	
	
	public void setEnabled(boolean on)
	{
		super.setEnabled(on);
	
		for(VKey k: UI.collectChildrenOfType(VKey.class, this))
		{
			k.setEnabled(on);
		}
	}
	

	public void toggleCapsLock()
	{
		capsLock = !capsLock;
		updateState();
	}
	
	
	public void setShift(boolean on)
	{
		shift = on;
		updateState();
	}
	
	
	public void toggleShift()
	{
		shift = !shift;
		updateState();
	}
	
	
	public void setAltGr(boolean on)
	{
		altGraph = on;
		updateState();
	}
	
	
	public void toggleAltGr()
	{
		altGraph = !altGraph;
		updateState();
	}
	
	
	protected void updateState()
	{
		int n;
		if(altGraph)
		{
			n = REGISTER_ALT;
		}
		else if(shift || capsLock)
		{
			n = REGISTER_UPPER;
		}
		else
		{
			n = REGISTER_NORMAL;
		}
		
		register = n;

		for(VKey k: UI.collectChildrenOfType(VKey.class, this))
		{
			k.updateRegister();
		}
	}
	

	public static CLanguageCode[] getSupportedLayouts()
	{
		return KeyboardLayouts.SUPPORTED_LAYOUTS;
	}


	public void change(CLanguageCode c)
	{
		removeAll();
		
		KeyboardLayouts.build(this, c);
		
		validate();
		repaint();
	}
	
	
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}
	
	
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}
	
	
	public static OnScreenKeyboard create()
	{
		return create(CLanguage.getDefault());
	}
	
	
	public static OnScreenKeyboard create(CLanguage la)
	{
		CLanguageCode c = (la == null ? null : la.getLanguageCode());
		if(c == null)
		{
			c = CLanguageCode.EnglishUS;
		}
		
		OnScreenKeyboard k = new OnScreenKeyboard();
		k.change(c);
		return k;
	}
}
