// Copyright (c) 2007-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AssignMnemonic;
import goryachev.common.ui.theme.ButtonPanelLayout;
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


public class CButtonPanel
	extends JPanel
{
	private JButton defaultButton;
	
	
	public CButtonPanel()
	{
		super(new ButtonPanelLayout());
	}
	
	
	// compatibility constructor
	public CButtonPanel(int gap, JButton ... bs)
	{
		this();
		getButtonPanelLayout().setGap(gap);
		
		for(JButton b: bs)
		{
			addButton(b);
		}
	}
	
	
	public CButtonPanel(int gap)
	{
		this();
		getButtonPanelLayout().setGap(gap);
	}
	
	
	// compatibility constructor
	public CButtonPanel(int gap, Action ... as)
	{
		this();
		getButtonPanelLayout().setGap(gap);
		
		for(Action a: as)
		{
			addButton(a);
		}
	}
	
	
	// compatibility constructor
	public CButtonPanel(JButton ... bs)
	{
		this();
		
		for(JButton b: bs)
		{
			addButton(b);
		}
	}
	
	
	public void addNotify()
	{
		AssignMnemonic.assign(this);
		super.addNotify();
	}
	
	
	protected ButtonPanelLayout getButtonPanelLayout()
	{
		return (ButtonPanelLayout)getLayout();
	}
	
	
	public void setLayout(LayoutManager m)
	{
		if(m instanceof ButtonPanelLayout)
		{
			super.setLayout(m);
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
		defaultButton = b;
		return b;
	}
	
	
	public void addSpace()
	{
		// TODO
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
		for(int i=getComponentCount() - 1; i>=0; --i)
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
