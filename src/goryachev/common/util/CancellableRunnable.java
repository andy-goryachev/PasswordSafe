// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public interface CancellableRunnable
	extends Runnable, Cancellable
{
	public void run();
	
	public void cancel();
	
	public boolean isCancelled();
}
