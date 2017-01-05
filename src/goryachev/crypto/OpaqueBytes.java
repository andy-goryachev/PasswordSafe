// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto;


/**
 * This class provides an opaque storage for a byte array.
 */
public class OpaqueBytes
	extends OpaqueMemObject
{
	public OpaqueBytes(byte[] b)
	{
		super(b);
	}
	
	
	public OpaqueBytes()
	{
	}
	
	
	public final byte[] getValue()
	{
		return getBytes();
	}
	
	
	public final void setValue(byte[] b)
	{
		setBytes(b);
	}
}
