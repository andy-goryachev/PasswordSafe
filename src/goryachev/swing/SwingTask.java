// Copyright © 2017-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import goryachev.common.log.Log;
import goryachev.common.util.CTask;
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
			log.error(e);
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
