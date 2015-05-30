// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** "RuntimeException" is too long */
public class Rex
	extends RuntimeException
{
	public Rex()
	{
	}
	
	
	public Rex(String message)
	{
		super(message);
	}
	
	
	public Rex(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	public Rex(Throwable cause)
	{
		super(cause);
	}
}
