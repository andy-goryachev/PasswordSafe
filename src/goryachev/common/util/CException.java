// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** RuntimeException with shorter class name */
public class CException
    extends RuntimeException
{
	public CException()
	{
		super();
	}


	public CException(String message)
	{
		super(message);
	}


	public CException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public CException(Throwable cause)
	{
		super(cause);
	}
}
