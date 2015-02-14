// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.ui.Progress;


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
}
