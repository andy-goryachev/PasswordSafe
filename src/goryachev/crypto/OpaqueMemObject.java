// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto;
import goryachev.common.util.Rex;


/**
 * This class provides additional level of security by storing only the encrypted value in memory.
 * To avoid also keeping the key in memory, the key is generated each time from information
 * that stays constant during JVM session per hashCode() contract.
 */
abstract class OpaqueMemObject
{
	private byte[] encrypted;
	
	
	protected OpaqueMemObject()
	{
	}
	
	
	protected OpaqueMemObject(byte[] b)
	{
		setBytes(b);
	}
	
	
	public final boolean isNull()
	{
		return (encrypted == null);
	}
	
	
	protected final void setBytes(byte[] value)
	{
		if(value == null)
		{
			encrypted = null;
		}
		else
		{
			try
			{
				encrypted = MemCrypt.encrypt(value);
			}
			catch(Exception e)
			{
				// should not happen
				throw new Rex(e);
			}
		}
	}
	
	
	protected final void setBytes(byte[] value, int off, int len)
	{
		byte[] v = new byte[len];
		try
		{
			System.arraycopy(value, off, v, 0, len);
			
			try
			{
				encrypted = MemCrypt.encrypt(value);
			}
			catch(Exception e)
			{
				// should not happen
				throw new Rex(e);
			}
		}
		finally
		{
			Crypto.zero(v);
		}
	}
	
	
	public final String toString()
	{
		return String.valueOf('*');
	}
	
	
	/**
	 * Returns decrypted byte array representing the stored object.
	 */
	public final byte[] getBytes()
	{
		if(encrypted == null)
		{
			return null;
		}
		else
		{
			try
			{
				byte[] b = MemCrypt.decrypt(encrypted);
				return b;
			}
			catch(Exception e)
			{
				// should not happen
				throw new Rex(e);
			}
		}
	}
	
	
	public final void clear()
	{
		Crypto.zero(encrypted);
		encrypted = null;
	}
}
