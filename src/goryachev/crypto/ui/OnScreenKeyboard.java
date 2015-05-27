// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto.ui;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.UI;
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
	
	// 0 - normal
	// 1 - shift
	// 2 - alt graph
	private int register;
	private boolean shift;
	private boolean capsLock;
	private boolean altGraph;
	private Color keyColor = new Color(255, 255, 240);
	private Color ctrlColor = UI.darker(keyColor, 0.85f);
	private Color hoverColor = Color.white;
	private Border normalBorder = new CBorder(Color.gray);
	private Border hoverBorder = new CBorder(Color.black);


	public OnScreenKeyboard()
	{
		setBorder(new CBorder(new Color(0, 0, 0, 32), 5, 5));
		setBackground(new Color(0, 0, 0, 16));
	}
	
	
	protected void paintComponent(Graphics g) 
	{
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		super.paintComponent(g);
	}
	
	
	public Color getHoverColor()
	{
		return hoverColor;
	}
	
	
	public Color getNormalColor()
	{
		return hoverColor;
	}
	
	
	public Color getCtrlColor()
	{
		return ctrlColor;
	}
	
	
	public Border getNormalBorder()
	{
		return normalBorder;
	}
	
	
	public Border getHoverBorder()
	{
		return hoverBorder;
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
