// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.wizard;
import goryachev.common.util.CancelledException;
import goryachev.common.util.Progress;


public interface BackgroundOperation
{
	public interface Monitor
	{
		public void print(String text);
		
		public void printError(String text);
		
		public boolean isCancelled();
	}
	
	//
	
	/** background operation body.  returns normally or throws an exception. */
	public void processBackgroundOperation(Monitor m) throws CancelledException, Exception;
	
	/** returns operation progress value of null if progress can not be determined. */
	public Progress getProgress();
	
	
	/** release all the resources after op completed normally or with an error */
	public void dispose();
}