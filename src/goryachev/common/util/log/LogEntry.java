// Copyright (c) 2006-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;


public class LogEntry
{
	private long timestamp = System.currentTimeMillis();
	private Throwable exception;
	private String text;
	private String caller;


	public LogEntry()
	{
	}
	
	
	public LogEntry(Throwable e)
	{
		this.exception = e;
	}
	
	
	public LogEntry(Throwable e, String text)
	{
		this.exception = e;
		this.text = text;
	}
	
	
	public LogEntry(String text)
	{
		this.text = text;
	}
	
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	
	public String getText()
	{
		return text;
	}
	
	
	public Throwable getException()
	{
		return exception;
	}
	
	
	public boolean isException()
	{
		return (exception != null);
	}
	
	
	public String getTitle()
	{
		if(isException())
		{
			return exception.getClass().getName();
		}
		else
		{
			return text;
		}
	}


	public String getCaller()
	{
		return caller;
	}
	
	
	public void setCaller(Throwable ex, int level)
	{
		try
		{
			StackTraceElement[] ss = ex.getStackTrace();
			if(ss != null)
			{
				if(ss.length > level)
				{
					StackTraceElement t = ss[level];
					String className = t.getClassName();
			
					int ix = className.lastIndexOf('.');
					if(ix > 0)
					{
						className = className.substring(ix + 1);
					}
			
					caller =  className + "." + t.getMethodName();
				}
			}
		}
		catch(Exception e)
		{
			// getting AIOOBE
			// problem with stack trace array?
			e.printStackTrace();
		}
	}
}
