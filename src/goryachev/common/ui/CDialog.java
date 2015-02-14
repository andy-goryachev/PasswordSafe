// Copyright (c) 2005-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Log;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.Border;


public class CDialog
	extends JDialog
{
	protected void onOk() { }
	
	/** called after window appears on screen */
	public void onWindowOpened() { }
	
	/** called on close() */
	public void onWindowClosed() { }
	
	// return true if ok to close
	public boolean onWindowClosing() { return true; }
	
	// no client code: called from a constructor
	public boolean closeOnEscape() { return true; }
	
	//
	
	public static final int DEFAULT_WIDTH  = 700;
	public static final int DEFAULT_HEIGHT = 550;
	public static final int MARGIN = 10;

	public final CAction closeAction = new CAction() { public void action() { actionWindowClose(); } };
	public final CAction okAction = new CAction() { public void action() { onOk(); } };
	public static final Border defaultBorder = new CBorder(MARGIN);
	
	private Component defaultFocusComponent;
	protected final CPanel contentPanel;
	private CButtonPanel buttonPanel;

	//
	
	public CDialog(Component parent, String name, boolean modal)
	{
		super(UI.getParentWindow(parent));
		setMinimumSize(300, 200);
		setIcon(Application.getIcon());
		setModal(modal);
		setName(name);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent e)
			{
				focusDefaultComponent();
				onWindowOpened();
			}

			public void windowClosing(WindowEvent e)
			{
				if(onWindowClosing())
				{
					close();
				}
			}
		});
		
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel = new CPanel();
		contentPanel.setBorder(defaultBorder);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		if(closeOnEscape())
		{
			UI.whenAncestorOfFocusedComponent(getRootPane(), KeyEvent.VK_ESCAPE, closeAction);
		}
	}
	
	
	// AppTitle
	// Title - AppTitle
	public void setDialogTitle(String s)
	{
		String appTitle = Application.getTitle();
		String title;
		if(s == null)
		{
			title = appTitle;
		}
		else
		{
			title = s + " - " + appTitle;
		}
		setTitle(title);
	}
	
	
	protected void actionWindowClose()
	{
		if(onWindowClosing())
		{
			close();
		}
	}

	
	public void setDefaultFocusComponent(Component c)
	{
		defaultFocusComponent = c;
	}
	
	
	public void setDefaultButton(JButton b)
	{
		getRootPane().setDefaultButton(b);
	}
	
	
	protected void focusDefaultComponent()
	{
		if(defaultFocusComponent != null)
		{
			defaultFocusComponent.requestFocus();
		}
	}
	
	
	public void setIcon(Icon icon)
	{
		if(icon instanceof ImageIcon)
		{
			setIconImage(((ImageIcon)icon).getImage());
		}
	}
	
	
	public Window getParentWindow()
	{
		return UI.getParentWindow(getParent());
	}
	
	
	public CPanel getContentPanel()
	{
		return contentPanel;
	}
	
	
	public CButtonPanel getButtonPanel()
	{
		if(buttonPanel == null)
		{
			buttonPanel = createButtonPanel();
			contentPanel.setSouth(buttonPanel);
		}
		return buttonPanel;
	}
	
	
	protected CButtonPanel createButtonPanel()
	{
		CButtonPanel p = new CButtonPanel();
		p.setBorder(new CBorder(MARGIN, 0, 0, 0));
		return p;
	}
	
	
	public CButton addButton(Action a)
	{
		return getButtonPanel().addButton(a);
	}
	
	
	public CButton addButton(Action a, boolean highlight)
	{
		return getButtonPanel().addButton(a, highlight);
	}
	
	
	public void setContent(Component c)
	{
		getContentPanel().setCenter(c);
	}
	
	
	public void setContentPanel(Component c)
	{
		BorderLayout layout = (BorderLayout)getContentPane().getLayout();
		Component old = layout.getLayoutComponent(getContentPane(), BorderLayout.CENTER);
		if(old != null)
		{
			getContentPane().remove(old);
		}
		getContentPane().add(c, BorderLayout.CENTER);
	}
	
	
	public Window open()
	{
		GlobalSettings.opening(this);
		setVisible(true);
		toFront();
		return this;
	}


	public void close()
	{
		GlobalSettings.closing(this);
		
		try
		{
			// FIX this is incorrect
			onWindowClosed();
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		setVisible(false);
		dispose();
	}


	public JFrame getOwnerFrame()
	{
		return (JFrame)getOwner();
	}
	
	
	public void setMinimumSize(int w, int h)
	{
		setMinimumSize(new Dimension(w,h));
	}
	
	
	public void setPreferredSize(int w, int h)
	{
		setPreferredSize(new Dimension(w,h));
	}

	
	public void pack()
	{
		//super.pack();
		UI.setPreferredMinimumSize(this, getRootPane());
	}
	
	
	public JTextArea textArea()
	{
		JTextArea t = new JTextArea();
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		t.setFont(Theme.plainFont());
		t.setBorder(new CBorder(Color.gray, 4));
		return t;
	}
	
	
	public CScrollPane scroll(JComponent c)
	{
		CScrollPane sc = new CScrollPane(c);
		sc.getViewport().setBackground(Theme.textBG());
		return sc;
	}
	
	
	public CButton addButton(String text, Action a)
	{
		CButton b = new CButton(text, a);
		addButton(b);
		return b;
	}
	
	
	public void addButton(String text, Action a, boolean highlight)
	{
		addButton(new CButton(text, a, highlight));
	}
	
	
	public void addButton(String text, Action a, Color highlight)
	{
		addButton(new CButton(text, a, highlight));
	}
	
	
	public void addButton(JButton b)
	{
		getButtonPanel().addButton(b);
	}
	

	public void setDefaultButton()
	{
		JButton b = getButtonPanel().getLastButton();
		if(b != null)
		{
			setDefaultButton(b);
			setDefaultFocusComponent(b);
		}
	}
	
	
	public void setCenter(Component c)
	{
		contentPanel.setCenter(c);
	}
	
	
	public void setNorth(Component c)
	{
		contentPanel.setNorth(c);
	}
	
	
	public void setWest(Component c)
	{
		contentPanel.setWest(c);
	}
	
	
	public void setEast(Component c)
	{
		contentPanel.setEast(c);
	}
	
	
	public void setSouth(Component c)
	{
		contentPanel.setSouth(c);
	}
}
