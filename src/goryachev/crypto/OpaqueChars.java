// Copyright © 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.crypto;
import goryachev.common.util.CKit;


/**
 * This class provides an opaque storage for a char array.
 */
public final class OpaqueChars
	extends OpaqueMemObject
{
	public OpaqueChars(char[] cs)
	{
		set(cs);
	}
	
	
	public OpaqueChars()
	{
	}
	
	
	public void set(OpaqueChars x)
	{
		char[] cs = null;
		try
		{
			if(x != null)
			{
				cs = x.getChars();
			}
			
			set(cs);
		}
		finally
		{
			Crypto.zero(cs);
		}
	}
	
	
	public void set(String s)
	{
		char[] cs = null;
		try
		{
			if(s != null)
			{
				cs = s.toCharArray();
			}
			
			set(cs);
		}
		finally
		{
			Crypto.zero(cs);
		}
	}
	
	
	public final void set(char[] cs)
	{
		byte[] b = null;
		try
		{
			int sz = cs.length;
			b = new byte[sz + sz];
			int ix = 0;
			for(int i=0; i<sz; i++)
			{
				int c = cs[i];
				b[ix++] = (byte)(c >>> 8);
				b[ix++] = (byte)c;
			}
			
			setBytes(b);
		}
		finally
		{
			Crypto.zero(b);
		}
	}
	
	
	public final char[] getChars()
	{
		byte[] b = null;
		try
		{
			b = getBytes();
			int sz = b.length/2;
			
			char[] cs = new char[sz];
			int ix = 0;
			for(int i=0; i<sz; i++)
			{
				int c = (b[ix++] & 0xff) << 8;
				c |= (b[ix++] & 0xff);
				cs[i] = (char)c;
			}
			
			return cs;
		}
		finally
		{
			Crypto.zero(b);
		}
	}
	
	
	public boolean sameAs(OpaqueChars cs)
	{
		if(cs == null)
		{
			return false;
		}
		
		byte[] me = getBytes();
		try
		{
			byte[] him = cs.getBytes();
			try
			{
				return CKit.equals(me, him);
			}
			finally
			{
				Crypto.zero(him);
			}
		}
		finally
		{
			Crypto.zero(me);
		}
	}
}
