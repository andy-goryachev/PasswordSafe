// Copyright (c) 2007-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.CButtonPanelLayout3;
import goryachev.common.util.Rex;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;


// TODO
// - check default button in dialog/frame
// - right-align buttons if no FILL and no PERCENT
public class CButtonPanel3
	extends JPanel
{
	public static final int DEFAULT_GAP = 10;
	private JButton defaultButton;
	
	
	public CButtonPanel3()
	{
		this(DEFAULT_GAP, true);
	}
	
	
	public CButtonPanel3(int gap)
	{
		this(gap, true);
	}
	
	
	public CButtonPanel3(int gap, boolean opaque)
	{
		super(new CButtonPanelLayout3(gap));
		setOpaque(opaque);
		setMinimumButtonSize(Theme.minimumButtonWidth());
	}
	
	
	public void setBorder()
	{
		setBorder(Theme.BORDER_10);
	}
	
	
	public void setBorder(int sp)
	{
		setBorder(new CBorder(sp));
	}

	
	public void setBorder(int ver, int hor)
	{
		setBorder(new CBorder(ver, hor));
	}

	
	public void setMinimumButtonSize(int w)
	{
		buttonPanelLayout().setMinButtonSize(w);
	}
	
	
	protected CButtonPanelLayout3 buttonPanelLayout()
	{
		return (CButtonPanelLayout3)getLayout();
	}
	
	
	public void setLayout(LayoutManager m)
	{
		if(m instanceof CButtonPanelLayout3)
		{
			super.setLayout(m);
		}
		else
		{
			throw new Rex();
		}
	}
	
	
	public CButton addButton(String text, Action a)
	{
		CButton b = new CButton(text, a);
		addButton(b);
		return b;
	}
	
	
	public CButton addButton(String text, Action a, boolean highlight)
	{
		CButton b = new CButton(text, a);
		if(highlight)
		{
			b.setHighlight();
		}
		addButton(b);
		return b;
	}
	
	
	public CButton addButton(Action a, Color highlight)
	{
		CButton b = new CButton(a);
		b.setHighlight(highlight);
		addButton(b);
		return b;
	}
	
	
	public CButton addButton(String text, Action a, Color highlight)
	{
		CButton b = new CButton(text, a);
		b.setHighlight(highlight);
		addButton(b);
		return b;
	}
	

	public CButton addButton(Action a)
	{
		CButton b = new CButton(a);
		addButton(b);
		return b;
	}
	
	
	public CButton addButton(Action a, boolean highlight)
	{
		CButton b = new CButton(a);
		if(highlight)
		{
			b.setHighlight();
		}
		addButton(b);
		return b;
	}
	
	
	public JButton addButton(JButton b)
	{
		add(b);
		
		// TODO or simply find the last component?
		defaultButton = b;
		invalidate();
		
		return b;
	}


	public void fill()
	{
		buttonPanelLayout().addSpace(CButtonPanelLayout3.FILL);
	}


	public void space(int pixels)
	{
		buttonPanelLayout().addSpace(pixels);
	}


	public void space(float percent)
	{
		buttonPanelLayout().addSpace(percent);
	}


	public void registerDefaultButton(JFrame f)
	{
		registerDefaultButton(f.getRootPane());
	}
	
	
	public void registerDefaultButton(JDialog d)
	{
		registerDefaultButton(d.getRootPane());
	}
	
	
	public void registerDefaultButton(JRootPane rp)
	{
		if(defaultButton != null)
		{
			rp.setDefaultButton(defaultButton);
		}
	}
	
	
	public void setButtonMargin(Insets m)
	{
		for(Component c: getComponents())
		{
			if(c instanceof JButton)
			{
				((JButton)c).setMargin(m);
			}
		}
	}
	
	
	public JButton getLastButton()
	{
		for(int i=getComponentCount()-1; i>=0; --i)
		{
			Component c = getComponent(i);
			if(c instanceof JButton)
			{
				return (JButton)c;
			}
		}
		return null;
	}
	
	
	public int getButtonCount()
	{
		return UI.collectChildrenOfType(JButton.class, this).size();
	}
	
	
	/** 
	 * sets enabled state on a button with the specified action, if available.
	 * does nothing if the button can not be found 
	 */
	public void setButtonEnabled(Action a, boolean on)
	{
		if(a == null)
		{
			return;
		}
		
		for(Component c: getComponents())
		{
			if(c instanceof JButton)
			{
				JButton b = (JButton)c;
				if(b.getAction() == a)
				{
					b.setEnabled(on);
					return;
				}
			}
		}
	}
}
