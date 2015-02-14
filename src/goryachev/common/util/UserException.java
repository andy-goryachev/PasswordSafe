// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class UserException
    extends RuntimeException
{
	public UserException(String message)
	{
		super(message);
	}
	
	
	public UserException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
