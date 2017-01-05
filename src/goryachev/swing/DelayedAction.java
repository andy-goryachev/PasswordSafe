// Copyright © 2007-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.util.Log;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


// Swing delayed action
public abstract class DelayedAction
	extends Timer
{
	public abstract void action();

	public static final int DEFAULT_PERIOD = 500;

	//


	private static final ActionListener listener = new ActionListener()
	{
		public void actionPerformed(ActionEvent ev)
		{
			try
			{
				((DelayedAction)ev.getSource()).action();
			}
			catch(Exception e)
			{
				Log.ex(e);
			}
		}
	};


	public DelayedAction(int delay)
	{
		super(delay, listener);
		setRepeats(false);
	}
	
	
	public DelayedAction()
	{
		this(DEFAULT_PERIOD);
	}


	public void fire()
	{
		stop();
		start();
	}
	
	
	public void trigger()
	{
		fire();
	}
}
