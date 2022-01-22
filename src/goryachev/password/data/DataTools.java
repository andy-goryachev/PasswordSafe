// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.password.data;
import goryachev.common.util.CKit;
import goryachev.memsafecrypto.CCharArray;
import goryachev.memsafecrypto.Crypto;
import goryachev.memsafecrypto.OpaqueChars;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataTools
{
	public static final int MAX_STRING_LENGTH = 0x7fffff;
	
	
	private DataTools()
	{
	}


	public static void writeByte(OutputStream out, int d) throws Exception
	{
		out.write(d);
	}
	
	
	public static int readByte(InputStream in) throws Exception
	{
		int c = in.read();
		if(c < 0)
		{
			throw new EOFException();
		}
		return c & 0xff;
	}
	
	
	public static void writeInt(OutputStream out, int d) throws Exception
	{
		writeByte(out, d >>> 24);
		writeByte(out, d >>> 16);
		writeByte(out, d >>>  8);
		writeByte(out, d);
	}
	
	
	public static int readInt(InputStream in) throws Exception
	{
		int d = (readByte(in) << 24);
		d |= (readByte(in) << 16);
		d |= (readByte(in) << 8);
		d |= readByte(in);
		return d;
	}
	
	
	public static void writeLong(OutputStream out, long d) throws Exception
	{
		writeByte(out, (int)(d >>> 56));
		writeByte(out, (int)(d >>> 48));
		writeByte(out, (int)(d >>> 40));
		writeByte(out, (int)(d >>> 32));
		writeByte(out, (int)(d >>> 24));
		writeByte(out, (int)(d >>> 16));
		writeByte(out, (int)(d >>>  8));
		writeByte(out, (int)d);
	}
	
	
	public static long readLong(InputStream in) throws Exception
	{
		long d = ((long)readByte(in) << 56);
		d |= ((long)readByte(in) << 48);
		d |= ((long)readByte(in) << 40);
		d |= ((long)readByte(in) << 32);
		d |= ((long)readByte(in) << 24);
		d |= ((long)readByte(in) << 16);
		d |= ((long)readByte(in) << 8);
		d |= readByte(in);
		return d;
	}
	
	
	public static long getLong(byte[] b) throws Exception
	{
		long d = ((long)(b[0] & 0xff) << 56);
		d |= ((long)(b[1] & 0xff) << 48);
		d |= ((long)(b[2] & 0xff) << 40);
		d |= ((long)(b[3] & 0xff) << 32);
		d |= ((long)(b[4] & 0xff) << 24);
		d |= ((long)(b[5] & 0xff) << 16);
		d |= ((long)(b[6] & 0xff) << 8);
		d |= (b[7] & 0xff);
		return d;
	}


	public static void check(int value, int min, int max, String err) throws Exception
	{
		if(value < min)
		{
			throw new Exception(err);
		}
		else if(value > max)
		{
			throw new Exception(err);
		}
	}
	
	
	public static void writeObject(OutputStream out, Object x) throws Exception
	{
		if(x == null)
		{
			DataTools.writeByte(out, PassEntry.TYPE_NULL);
			DataTools.writeInt(out, 0);
		}
		else if(x instanceof String)
		{
			DataTools.writeByte(out, PassEntry.TYPE_STRING);
			writeString(out, (String)x);
		}
		else if(x instanceof OpaqueChars)
		{
			DataTools.writeByte(out, PassEntry.TYPE_OPAQUE);
			CCharArray cs = null;
			try
			{
				cs = ((OpaqueChars)x).getChars();
				writeChars(out, cs);
			}
			finally
			{
				Crypto.zero(cs);
			}
		}
		else
		{
			throw new Exception("unsupported type: " + x.getClass());
		}
	}
	
	
	public static Object readObject(InputStream in) throws Exception
	{
		int type = readByte(in);
		int sz = readInt(in);
		if(sz < 0)
		{
			throw new PassException(PassException.Error.CORRUPTED);
		}
		
		switch(type)
		{
		case PassEntry.TYPE_NULL:
			return null;
			
		case PassEntry.TYPE_STRING:
			return readString(in, sz);
			
		case PassEntry.TYPE_OPAQUE:
			CCharArray cs = null;
			try
			{
				cs = constructCharArray(sz);
				readChars(in, cs);
				return new OpaqueChars(cs);
			}
			finally
			{
				Crypto.zero(cs);
			}
			
		default:
			throw new PassException(PassException.Error.CORRUPTED);
		}
	}


	public static CCharArray constructCharArray(int sz) throws Exception
	{
		if(sz < 0)
		{
			throw new PassException(PassException.Error.CORRUPTED);
		}
		else if(CKit.isOdd(sz))
		{
			throw new PassException(PassException.Error.CORRUPTED);
		}

		int len = sz / 2;
		return new CCharArray(len);
	}


	public static void writeString(OutputStream out, String s) throws Exception
	{
		int sz = s.length();
		if(sz > MAX_STRING_LENGTH)
		{
			throw new PassException(PassException.Error.STRING_TOO_LONG);
		}
		
		writeInt(out, sz + sz); // 2 bytes per char
		
		for(int i=0; i<sz; i++)
		{
			int d = s.charAt(i);
			writeByte(out, d >>> 8);
			writeByte(out, d);
		}
	}
	
	
	public static String readString(InputStream in, int sz) throws Exception
	{
		if((sz % 2) != 0)
		{
			throw new PassException(PassException.Error.CORRUPTED);
		}
		sz /= 2;
		
		char[] cs = new char[sz];
		try
		{
			for(int i=0; i<sz; i++)
			{
				int c = readByte(in) << 8;
				c |= readByte(in);
				cs[i] = (char)c;
			}
			return new String(cs);
		}
		finally
		{
			// pointless, leaks through String
			Crypto.zero(cs);
		}
	}
	
	
	public static void writeChars(OutputStream out, CCharArray cs) throws Exception
	{
		int sz = cs.length();
		if(sz > MAX_STRING_LENGTH)
		{
			throw new PassException(PassException.Error.STRING_TOO_LONG);
		}
		
		writeInt(out, sz + sz); // 2 bytes per char
		
		for(int i=0; i<sz; i++)
		{
			int d = cs.get(i);
			writeByte(out, d >>> 8);
			writeByte(out, d);
		}
	}

	
	public static void readChars(InputStream in, CCharArray cs) throws Exception
	{
		int sz = cs.length();
		for(int i=0; i<sz; i++)
		{
			int c = readByte(in) << 8;
			c |= readByte(in);
			cs.set(i, (char)c);
		}
	}
}
