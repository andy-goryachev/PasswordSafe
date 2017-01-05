// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.password.ui;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


public abstract class ActivityMonitor
	implements AWTEventListener, ActionListener
{
	protected abstract void onNoActivity();
	
	//
	
	private final Window parent;
	private Timer timer;
	
	
	public ActivityMonitor(Window parent, int delay)
	{
		this.parent = parent;
		setDelay(delay);
	}
	
	
	public void start()
	{
		long mask = 
			AWTEvent.MOUSE_EVENT_MASK | 
			AWTEvent.MOUSE_WHEEL_EVENT_MASK | 
			AWTEvent.KEY_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(this, mask);
	}
	
	
	public void stop()
	{
		Toolkit.getDefaultToolkit().removeAWTEventListener(this);
	}
	
	
	protected void trigger()
	{
		if(timer != null)
		{
			timer.restart();
		}
	}


	public void eventDispatched(AWTEvent ev)
	{
		trigger();
	}


	public void actionPerformed(ActionEvent ev)
	{
		if(parent.isVisible())
		{
			onNoActivity();
		}
		else
		{
			stop();
			stopTimer();
		}
	}
	
	
	public void setDelay(int delay)
	{
		stopTimer();
		
		timer = new Timer(delay, this);
		timer.setRepeats(false);
		timer.start();
	}
	
	
	protected void stopTimer()
	{
		if(timer != null)
		{
			timer.stop();
			timer = null;
		}		
	}
}
