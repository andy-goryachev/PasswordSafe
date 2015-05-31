// Copyright (c) 2011-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto;
import goryachev.common.util.CException;
import goryachev.common.util.CKit;


/**
 * This class provides an opaque storage for a String.
 */
public final class OpaqueString
	extends OpaqueMemObject
{
	public OpaqueString(String s)
	{
		set(s);
	}
	
	
	public OpaqueString()
	{
	}
	
	
	public final void set(String s)
	{
		setBytes(s.getBytes(CKit.CHARSET_UTF8));
	}
	
	
	public final String getString()
	{
		try
		{
			return new String(getBytes(), CKit.CHARSET_UTF8);
		}
		catch(Exception e)
		{
			// should not happen
			throw new CException(e);
		}
	}
}