// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.AnimatedWaitPanel;
import goryachev.common.ui.dialogs.StandardDialogPanel;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.Activable;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import javax.swing.text.Document;


public class BaseDialog
	extends JDialog
{
	/** called after window appears on screen */
	public void onWindowOpened() { }

	/** called on close() */
	public void onWindowClosed() { }

	// return true if ok to close
	public boolean onWindowClosing() { return true; }

	//

	protected final CAction backAction = new CAction() { public void action() { actionBack(); }};
	public final CAction closeDialogAction = new CAction() { public void action() { close(); } };
	protected final CAction cancelAction = new CAction() { public void action() { actionCancel(); }};
	protected final CAction escapeAction = new CAction() { public void action() { actionOnEscapeKey(); } };
	
	protected final CPanel cardHolder;
	private boolean closeOnEscape = true;
	protected BackgroundThread thread;
	protected Component card;
	private boolean backButtonEnabled = true;
	

	public BaseDialog(Component parent, String name, boolean modal)
	{
		super(UI.getParentWindow(parent));
		
		setMinimumSize(300, 200);
		setSize(700, 550);
		
		setIcon(Application.getIcon());
		setModal(modal);

		setName(name);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter()
		{
			public void windowOpened(WindowEvent ev)
			{
				//focusDefaultComponent();
				onWindowOpened();
			}


			public void windowClosing(WindowEvent ev)
			{
				if(onWindowClosing())
				{
					close();
				}
			}
		});

		getContentPane().setLayout(new BorderLayout());

		cardHolder = new CPanel();
		getContentPane().add(cardHolder, BorderLayout.CENTER);

		//UI.whenInFocusedWindow(getRootPane(), KeyEvent.VK_ESCAPE, escapeAction);
	}
	
	
	public void setBackButtonEnabled(boolean on)
	{
		backButtonEnabled = on;
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


	public void setDefaultButton(JButton b)
	{
		getRootPane().setDefaultButton(b);
	}


	public void setIcon(Icon icon)
	{
		if(icon instanceof ImageIcon)
		{
			setIconImage(((ImageIcon)icon).getImage());
		}
	}


	public CPanel getCardHolder()
	{
		return cardHolder;
	}


	public void setCenter(Component c)
	{
		if(this.card == null)
		{
			this.card = c;
		}

		getCardHolder().setCenter(c);
		getCardHolder().validate();
		getCardHolder().repaint();
		
		if(c instanceof Activable)
		{
			((Activable)c).activate();
		}
	}
	
	
	protected void actionBack()
	{
		if(card != null)
		{
			setCenter(card);
		}
	}


	public void open()
	{
		GlobalSettings.opening(this);
		setVisible(true);
		toFront();
	}


	public void close()
	{
		GlobalSettings.closing(this);

		try
		{
			onWindowClosed();
		}
		catch(Exception e)
		{
			Log.err(e);
		}

		setVisible(false);
		dispose();
	}


	public void setMinimumSize(int w, int h)
	{
		setMinimumSize(new Dimension(w, h));
	}


	public void setPreferredSize(int w, int h)
	{
		setPreferredSize(new Dimension(w, h));
	}


	public void pack()
	{
		//super.pack();
		UI.setPreferredMinimumSize(this, getRootPane());
	}

	
	public boolean isCloseOnEscape()
	{
		return closeOnEscape;
	}
	
	
	protected void actionOnEscapeKey()
	{
		if(closeOnEscape)
		{
			close();
		}
	}
	
	
	/** this enabled (a page) to close the window on ESC key */
	public void closeOnEscape(JComponent c)
	{
		UI.whenInFocusedWindow(c, KeyEvent.VK_ESCAPE, escapeAction);
	}
	
	
	public void setCloseOnEscape()
	{
		UI.whenInFocusedWindow(getRootPane(), KeyEvent.VK_ESCAPE, escapeAction);
	}

	
	/** constructs a standard success page */
	public StandardDialogPanel constructSuccessPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Success96);
		return p;
	}
	
	
	/** constructs a standard error page */
	public StandardDialogPanel constructErrorPage(Throwable err)
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Error96);
		p.setTextError(err);
		return p;
	}
	
	
	/** constructs a standard cancelled page */
	public StandardDialogPanel constructCancelledPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Cancelled96);
		return p;
	}
	
	
	/** constructs a standard info page */
	public StandardDialogPanel constructInfoPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Info96);
		return p;
	}
	
	
	/** constructs a standard warning page */
	public StandardDialogPanel constructWarningPage()
	{
		StandardDialogPanel p = new StandardDialogPanel();
		p.setIcon(CIcons.Warning96);
		return p;
	}
	
	
	public void cancel()
	{
		if(thread != null)
		{
			thread.cancel();
		}
	}
	
	
	protected void actionCancel()
	{
		// can ask for confirmation
		cancel();
	}
	
	
	protected Component createAnimationPage()
	{
		AnimatedWaitPanel p = new AnimatedWaitPanel(96);
		p.setBorder(StandardDialogPanel.LINE_BORDER);
		return p;
	}
	
	
	public void startOperation(final BaseOperation op)
	{
		Component c = op.getOperationViewer();
		if(c == null)
		{
			c = createAnimationPage();
		}
		
		BasePanel p = new BasePanel();
		p.setCenter(c);
		p.buttonPanel().add(new CButton(Menus.Cancel, cancelAction));
		
		thread = new BackgroundThread(op.getName())
		{
			private Object rv;
			
			public void process() throws Throwable
			{
				rv = op.executeOperation();
			}
			
			public void success()
			{
				if(isCancelled())
				{
					operationCancelled();
				}
				else
				{
					operationSuccess(rv);
				}
			}
			
			public void onError(Throwable e)
			{
				if(isCancelled())
				{
					operationCancelled();
				}
				else
				{
					operationError(e);
				}
			}
		};
		
		setCenter(p);
		
		thread.start();
	}
	
	
	protected void operationSuccess(Object rv)
	{
		StandardDialogPanel p = constructSuccessPage();
		if(backButtonEnabled) // is this needed?
		{
			p.buttons().add(new CButton(Menus.Back, backAction));
		}
		p.buttons().add(new CButton(Menus.Close, closeDialogAction, true));
		
		if(rv instanceof String)
		{
			String txt = (String)rv;
			if(CKit.startsWithIgnoreCase(txt, "<html>"))
			{
				p.setTextHtml(txt);
			}
			else
			{
				p.setTextPlain(txt);
			}
		}
		else if(rv instanceof Component)
		{
			p.setCenter((Component)rv);
		}
		else if(rv instanceof Document)
		{
			p.setTextDocument((Document)rv);
		}
		else
		{
			p.setTextPlain(CKit.toString(rv));
		}
		
		closeOnEscape(p);
		setCenter(p);
	}
	
	
	protected void operationCancelled()
	{
		StandardDialogPanel p = constructCancelledPage(); 
		if(backButtonEnabled)
		{
			p.buttons().add(new CButton(Menus.Back, backAction));
		}
		p.buttons().add(new CButton(Menus.Close, closeDialogAction, true));
		
		closeOnEscape(p);
		setCenter(p);
	}
	

	protected void operationError(Throwable err)
	{
		StandardDialogPanel p = constructErrorPage(err);
		if(backButtonEnabled)
		{
			p.buttons().add(new CButton(Menus.Back, backAction));
		}
		p.buttons().add(new CButton(Menus.Close, closeDialogAction, true));
		
		closeOnEscape(p);
		setCenter(p);
	}
}
