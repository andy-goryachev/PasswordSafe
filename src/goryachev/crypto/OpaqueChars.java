// Copyright © 2011-2017 Andy Goryachev <andy@goryachev.com>
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


	public void append(String s)
	{
		if(s != null)
		{
			append(s.toCharArray());
		}
	}
	
	
	public void append(char[] add)
	{
		char[] cs = getChars();
		try
		{
			char[] rv = new char[cs.length + add.length];
			try
			{
				System.arraycopy(cs, 0, rv, 0, cs.length);
				System.arraycopy(add, 0, rv, cs.length, add.length);
				set(rv);
			}
			finally
			{
				Crypto.zero(rv);
			}
		}
		finally
		{
			Crypto.zero(cs);
		}
	}


	public void deleteLastChar()
	{
		char[] cs = getChars();
		try
		{
			int len = cs.length - 1;
			if(len >= 0)
			{
				// TODO this does not handle surrogate characters
				char[] rv = new char[len];
				try
				{
					System.arraycopy(cs, 0, rv, 0, len);
					set(rv);
				}
				finally
				{
					Crypto.zero(rv);
				}
			}
			
		}
		finally
		{
			Crypto.zero(cs);
		}
	}
}
