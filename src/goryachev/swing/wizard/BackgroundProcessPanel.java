// Copyright © 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.wizard;
import goryachev.common.i18n.Menus;
import goryachev.common.i18n.TXT;
import goryachev.common.util.BackgroundOperation;
import goryachev.common.util.CKit;
import goryachev.common.util.CancelledException;
import goryachev.common.util.Log;
import goryachev.common.util.Progress;
import goryachev.common.util.UserException;
import goryachev.swing.BackgroundThread;
import goryachev.swing.CBorder;
import goryachev.swing.CIcon;
import goryachev.swing.CPanel;
import goryachev.swing.CScrollPane;
import goryachev.swing.CTextPane;
import goryachev.swing.CToolBar;
import goryachev.swing.Dialogs;
import goryachev.swing.ProgressLogic;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.XAction;
import goryachev.swing.icons.CIcons;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public abstract class BackgroundProcessPanel
	extends CPanel 
	implements BackgroundOperation.Monitor 
{
	protected abstract void updateActions();
	
	//
	
	public final XAction cancelAction = new XAction(this::actionCancel);
	public final JLabel statusField;
	public final CTextPane textField;
	protected BackgroundOperation operation; 
	private BackgroundThread thread;
	private Timer timer;
	protected int period = 450;
	protected ProgressLogic progressLogic;
	protected Throwable error;
	protected boolean cancelled;
	
	
	public BackgroundProcessPanel(BackgroundOperation op)
	{
		this.operation = op;
		
		statusField = new JLabel(" ", new CIcon(32), JLabel.LEFT);
		statusField.setIconTextGap(10);
		statusField.setBorder(new CBorder(5));
		
		progressLogic = new ProgressLogic();

		timer = new Timer(period, new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				onTimerEvent();
			}
		});

		textField = new CTextPane();
		textField.setEditable(false);
		textField.setScrollableTracksViewportWidth(true);
		
		CScrollPane scroll = new CScrollPane(textField);
		
		CToolBar tb = Theme.toolbar();
		tb.space(5);
		tb.fill(statusField);
		
		setNorth(tb);
		setCenter(scroll);
	}

	
	protected void onTimerEvent()
	{
		if(operation != null)
		{
			Progress p = operation.getProgress();
			if(p != null)
			{
				progressLogic.setProgress(p);
				statusField.setText(TXT.get("BackgroundProcessPage.processing PERCENT - TIME remaining", "Processing... {0} - {1} remaining.", p.getPercentString(), progressLogic.getEstimatedTimeRemaining()));
			}
		}
	}
	
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	
	public boolean isRunning()
	{
		return (thread != null);
	}
	
	
	public boolean isError()
	{
		return (error != null);
	}
	
	
	public void start()
	{
		if(thread != null)
		{
			return;
		}
		
		textField.setText(null);
		cancelAction.setEnabled(true);

		if(operation == null)
		{
			return;
		}

		setStatus(Theme.waitIcon(32), Menus.Processing);
		timer.start();
		
		// background process
		thread = new BackgroundThread("ProcessPage")
		{
			public void process() throws Throwable
			{
				progressLogic.setStart(getStartTime());
				
				// process body
				operation.processBackgroundOperation(BackgroundProcessPanel.this);
				
				comfortSleep();
			}
			
			public void success()
			{
				setResult(this, null);
				
			}
			
			public void onError(Throwable e)
			{
				setResult(this, e);
			}
		};
		thread.start();
		
		updateActions();
	}
	
	
	protected void setResult(BackgroundThread t, Throwable err)
	{
		if(t == thread)
		{
			thread = null;
			timer.stop();
			
			operation.dispose();
			operation = null;
			
			if(err == null)
			{
				setStatus(CIcons.Success32, TXT.get("BackgroundProcessPage.process completed", "Completed"));
			}
			else if(CancelledException.is(err))
			{
				setStatus(CIcons.Cancelled32, TXT.get("BackgroundProcessPage.process.cancelled", "Cancelled"));
			}
			else
			{
				error = err;
				
				setStatus(CIcons.Error32, TXT.get("BackgroundProcessPage.process.failed", "Failed"));
				
				if(err instanceof UserException)
				{
					printError(err.getMessage());
				}
				else
				{
					Log.ex(err);
					printError(CKit.stackTrace(err));
				}
			}
			
			cancelAction.setEnabled(false);
			updateActions();
		}
	}
	
	
	public void print(final Color c, final String text)
	{
		UI.inEDT(new Runnable()
		{
			public void run()
			{
				try
				{
					Document d = textField.getDocument();
					SimpleAttributeSet as = new SimpleAttributeSet();
					StyleConstants.setForeground(as, c);
					d.insertString(d.getLength(), text + "\n", as);
					
					Rectangle r = textField.modelToView(d.getLength());
					textField.scrollRectToVisible(r);
				}
				catch(Exception e)
				{
					Log.ex(e);
				}
			}
		});
	}
	
	
	public void print(String text)
	{
		print(Theme.TEXT_FG, text);
	}
	
	
	public void printError(String text)
	{
		print(Color.red, text);
	}
	
	
	protected void setStatus(Icon icon, String text)
	{
		statusField.setIcon(icon);
		statusField.setText(text);
	}
	
	
	/** 
	 * sets enabled state on a button with the specified action, if available.
	 * does nothing if the button can not be found 
	 */
	public void setButtonEnabled(Action a, boolean on)
	{
		if(hasButtonPanel())
		{
			buttonPanel().setButtonEnabled(a, on);
		}
	}
	
	
	protected void actionCancel()
	{
		if(Dialogs.confirmInterruption(this))
		{
			cancel();
		}
	}
	
	
	public void cancel()
	{
		if(thread != null)
		{
			thread.cancel();
			cancelled = true;
			thread = null;
			
			setStatus(CIcons.Cancelled32, TXT.get("BackgroundProcessPage.process.interrupted", "Interrupted"));
			print(Color.blue, TXT.get("BackgroundProcessPage.interrupted", "Operation interrupted by user."));
			updateActions();
		}
	}
}
