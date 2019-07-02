// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.util.CTask;
import goryachev.common.util.Log;
import java.awt.EventQueue;


/**
 * Swing CTask.
 */
public class SwingTask<T>
	extends CTask<T>
{
	public SwingTask()
	{
	}
	
	
	protected void handleSuccess(T result)
	{
		if(onSuccess != null)
		{
			EventQueue.invokeLater(() -> super.handleSuccess(result));
		}
	}
	
	
	protected void handleError(Throwable e)
	{
		if(onError == null)
		{
			Log.ex(e);
		}
		else
		{
			EventQueue.invokeLater(() -> super.handleError(e));
		}
	}
	
	
	protected void handleFinish()
	{
		if(onFinish != null)
		{
			EventQueue.invokeLater(() -> super.handleFinish());
		}
	}
}
