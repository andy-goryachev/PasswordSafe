// Copyright © 2007-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.log.Log;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


// Swing delayed action
public class DelayedActionSwing
	extends Timer
{
	protected static final Log log = Log.get("DelayedActionSwing");


	public DelayedActionSwing(int delay, Runnable callback)
	{
		super(delay, new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				try
				{
					callback.run();
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
		});
		
		setRepeats(false);
	}
	
	
	public void action()
	{
		fireActionPerformed(null);
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
	
	
	public void cancel()
	{
		stop();
	}
}
