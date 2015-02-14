// Copyright (c) 2011 Andy Goryachev <andy@goryachev.com>
package goryachev.password.ui;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


public abstract class ActivityMonitor
	implements AWTEventListener, ActionListener
{
	protected abstract void onNoActivity();
	
	//
	
	private Timer timer;
	
	
	public ActivityMonitor(int delay)
	{
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
		timer.restart();
	}


	public void eventDispatched(AWTEvent ev)
	{
		trigger();
	}


	public void actionPerformed(ActionEvent ev)
	{
		onNoActivity();
	}
	
	
	public void setDelay(int delay)
	{
		if(timer != null)
		{
			timer.stop();
		}
		
		timer = new Timer(delay, this);
		timer.setRepeats(false);
	}
}
