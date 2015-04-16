// Copyright (c) 2007-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Map;
import javax.swing.text.AttributeSet;


/** debugging printing */
public class D
{
	public static final Color red = Color.red;
	public static final String HEX = "0123456789ABCDEF";
	
	
	private D()
	{ }

	
	public static String toHexString(int d, int digits)
	{
		char[] buf = new char[digits];
		while(--digits >= 0)
		{
			buf[digits] = HEX.charAt(d & 0x0f);
			d >>= 4;
		}
		return new String(buf);
	}
	
	
	public static String toHexString(int d)
	{
		return toHexString(d,8);
	}
	
	
	public static String toHexString(short d)
	{
		return toHexString(d,4);
	}
	
	
	public static String toHexString(long d, int digits)
	{
		char[] buf = new char[digits];
		while(--digits >= 0)
		{
			buf[digits] = HEX.charAt((int)(d & 0x0f));
			d >>= 4;
		}
		return new String(buf);
	}
	
	
	public static String toHexString(long d)
	{
		return toHexString(d,16);
	}
	
	
	public static String toHexString(byte[] b)
	{
		return toHexString(b, 0, b.length);
	}
	

	public static String toHexString(byte[] b, int start, int length)
	{
		SB sb = new SB(b.length);
		int end = start + length;
		for(int i=start; i<end; i++)
		{
			int x = b[i];
			sb.append(HEX.charAt((x >> 4) & 0x0f));
			sb.append(HEX.charAt(x & 0x0f));
		}
		return sb.toString();
	}
	
	
	public static char toHex(int nibble)
	{
		return HEX.charAt(nibble & 0x0f);
	}
	
	
	// returns parsed byte array (all hex, trimmed string contains exactly 2N hex characters)
	// or null
	public static byte[] parseByteArray(String s)
	{
		if(s == null)
		{
			return null;
		}
		
		s = s.trim();
		int len = s.length() / 2;
		if(len + len != s.length())
		{
			return null;
		}
		
		byte[] a = new byte[len];
		int ix = 0;
		try
		{
			for(int i=0; i<len; i++)
			{
				int d = parseHex(s.charAt(ix++)) << 4;
				a[i] = (byte)(d | parseHex(s.charAt(ix++)));
			}
		}
		catch(Exception e)
		{
			return null;
		}
		
		return a;
	}
	
	
	public static int parseHex(char c) throws Exception
	{
		int x = HEX.indexOf(Character.toUpperCase(c));
		if(x < 0)
		{
			throw new Exception("not a hexadecimal character: " + c);
		}
		return x;
	}
	
	
	// convert milliseconds to MM:SS or HHH:MM:SS String
	public static String msToString(long ms)
	{
		StringBuffer sb = new StringBuffer();
		long n;
		ms /= 1000;

		if((n = ms/3600) != 0)
		{
			sb.append(n);
			sb.append(":");
		}

		ms %= 3600;

		sb.append(ms/600);
		ms %= 600;
		sb.append(ms/60);
		sb.append(':');
		ms %= 60;
		sb.append(ms/10);
		ms %= 10;
		sb.append(ms);

		return sb.toString();
	}


	/**
	 * Writes a formatted string to this object's destination using the
	 * specified format string and arguments.
	 *
	 * @param  format
	 *         A format string per {@link java.util.Formatter} rules.
	 *
	 * @param  args
	 *         Arguments referenced by the format specifiers in the format
	 *         string.  If there are more arguments than format specifiers, the
	 *         extra arguments are ignored.  The maximum number of arguments is
	 *         limited by the maximum dimension of a Java array as defined by
	 *         <cite>The Java&trade; Virtual Machine Specification</cite>.
	 */
	public static void printf(String format, Object... args)
	{
		String s = String.format(format, args);
		log(s, 2);
	}


	public static void print()
	{
		log("", 2);
	}


	public static void print(Object a)
	{
		log(a == null ? "null" : a.toString(), 2);
	}
	
	
	public static void print(Object ... a)
	{
		SB sb = new SB();
		for(Object x: a)
		{
			if(sb.length() > 0)
			{
				sb.append(" ");
			}
			sb.append(x);
		}
		log(sb.toString(), 2);
	}
	
	
	/** list content of a collection-type or array-type object */
	public static void list(Object x)
	{
		if(x instanceof Map)
		{
			listMap((Map)x);
		}
		else if(x instanceof Iterable)
		{
			listIterable((Iterable)x);
		}
		else if(x instanceof Enumeration)
		{
			listEnumeration((Enumeration)x);
		}
		else if(x instanceof Object[])
		{
			listObjectArray((Object[])x);
		}
		else if(x instanceof int[])
		{
			listIntArray((int[])x);
		}
		else if(x instanceof long[])
		{
			listLongArray((long[])x);
		}
		else if(x instanceof AttributeSet)
		{
			listAttributeSet((AttributeSet)x);
		}
		else 
		{
			print(x);
		}
	}
	
	
	private static void listMap(Map<?,?> a)
	{
		SB sb = new SB();
		sb.append(a.size());
		
		CList<Object> keys = new CList(a.keySet());
		CSorter.sort(keys);
		
		for(Object key: keys)
		{
			sb.append("\n");
			sb.append("    ");
			sb.append(key);
			sb.append(": ");
			sb.append(a.get(key));
		}

		log(sb.toString(), 2);
	}
	
	
	private static void listIterable(Iterable<?> a)
	{
		SB sb = new SB();

		for(Object d: a)
		{
			sb.append("\n");
			sb.append("    ");
			sb.append(d);
		}

		log(sb.toString(), 2);
	}
	
	
	private static void listEnumeration(Enumeration<?> a)
	{
		SB sb = new SB();

		while(a.hasMoreElements())
		{
			sb.append("\n");
			sb.append("    ");
			sb.append(a.nextElement());
		}

		log(sb.toString(), 2);
	}
	
	
	private static void listObjectArray(Object[] a)
	{
		SB sb = new SB();
		sb.append(a.length);
		
		for(Object d: a)
		{
			sb.append("\n");
			sb.append("    ");
			sb.append(d);
		}

		log(sb.toString(), 2);
	}
	
	
	private static void listIntArray(int[] a)
	{
		SB sb = new SB();
		sb.append(a.length);
		
		for(int d: a)
		{
			sb.append("\n");
			sb.append("    ");
			sb.append(d);
		}

		log(sb.toString(), 2);
	}
	
	
	private static void listLongArray(long[] a)
	{
		SB sb = new SB();
		sb.append(a.length);
		
		for(long d: a)
		{
			sb.append("\n");
			sb.append("    ");
			sb.append(d);
		}

		log(sb.toString(), 2);
	}
	
	
	private static void listAttributeSet(AttributeSet as)
	{
		SB sb = new SB();
		sb.append(as.getAttributeCount());
		
		Enumeration en = as.getAttributeNames();
		while(en.hasMoreElements())
		{
			Object att = en.nextElement();
			sb.append("\n");
			sb.append("    ");
			sb.append(att);
		}

		log(sb.toString(), 2);
	}

	
	public static void trace()
	{
		StringWriter sw = new StringWriter();
		PrintWriter out = new PrintWriter(sw);

		out.println();
		StackTraceElement[] trace = new Throwable().getStackTrace();
		for(int i=1; i<trace.length; i++)
		{
			out.println("\t" + trace[i]);
		}

		log(sw.toString(),3);
	}
	
	
	public static void err(Throwable e)
	{
		log(stackTrace(e), 2);
	}
	
	
	public static void err(Object msg)
	{
		log(msg + "\n" + stackTrace(new Throwable(), 1), 2);
	}
	

	public static String stackTrace(Throwable e)
	{
		StringWriter os = new StringWriter();
		e.printStackTrace(new PrintWriter(os));
		return os.toString();
	}
	
	
	public static String stackTrace(Throwable e, int level)
	{
		SB sb = new SB();
		StackTraceElement[] t = e.getStackTrace();
		for(int i=level; i<t.length; i++)
		{
			sb.a("\tat ").a(t[i]).a('\n');
		}
		return sb.toString();
	}

	
	private static String getClassName(StackTraceElement t)
	{
		String s = t.getClassName();
		int ix = s.lastIndexOf('.');
		if(ix >= 0)
		{
			return s.substring(ix+1, s.length());
		}
		return s;
	}
	
	
	private static void log(String msg, int depth)
	{
		StackTraceElement t = new Throwable().getStackTrace()[depth];
		String className = getClassName(t);
		System.out.println(className + "." + t.getMethodName() + " " + msg);
	}


	public static String toString(Object a)
	{
		return (a == null ? null : a.toString());
	}


	public static int hashCode(Object ... xs)
	{
		return hashCodeArray(xs);
	}


	public static int hashCodeArray(Object[] xs)
	{
		int c = 0;
		for(Object x: xs)
		{
			if(c == 0)
			{
				c = x.hashCode();
			}
			else
			{
				c ^= x.hashCode();
			}
		}
		return c;
	}
	
	
	public static String toString(byte[] bytes, String encoding) throws Exception
	{
		return (encoding == null ? new String(bytes) : new String(bytes, encoding));
	}

	
	// dumps byte array into a nicely formatted String
	// printing address first, then 16 bytes of hex then ascii representation then newline
	//     "0000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" or
	// "00000000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" 
	// depending on startAddress
	// address starts with startAddress
	public static String toHexStringAscii(byte[] bytes)
	{
		long startAddress = 0;
		boolean bigfile = ((startAddress + bytes.length) > 65535);
		SB sb = new SB(((bytes.length/16)+1) * 77 + 1);
		
		int col = 0;
		long addr = startAddress;
		int lineStart = 0;

		for(int i=0; i<bytes.length; i++)
		{
			// offset
			if(col == 0)
			{
				lineStart = i;
				if(bigfile)
				{
					hex(sb,(int)(addr >> 24));
					hex(sb,(int)(addr >> 16));
				}
				hex(sb,(int)(addr >> 8));
				hex(sb,(int)(addr));
				sb.append("  ");
			}
			
			// byte
			hex(sb,bytes[i]);
			sb.append(' ');

			// space or newline
			if(col >= 15)
			{
				dumpAscii(sb,bytes,lineStart);
				col = 0;
			}
			else
			{
				col++;
			}
			
			addr++;
		}
		
		if(col != 0)
		{
			while(col++ < 16)
			{
				sb.append("   ");
			}

			dumpAscii(sb,bytes,lineStart);
		}
		
		return sb.toString();
	}
	
	
	private static void hex(SB sb, int c)
	{
		sb.append(HEX.charAt((c >> 4) & 0x0f));
		sb.append(HEX.charAt(c & 0x0f));
	}
	
	
	private static void dumpAscii(SB sb, byte[] bytes, int lineStart)
	{
		// first, print padding
		sb.append(' ');
	
		int max = Math.min(bytes.length,lineStart+16);
		for(int i=lineStart; i<max; i++)
		{
			int d = bytes[i] & 0xff;
			if((d < 0x20) || (d >= 0x7f))
			{
				d = '.';
			}
			sb.append((char)d);
		}
		
		sb.append('\n');
	}
	
	
	public static void dump(byte[] b)
	{
		if(b == null)
		{
			print("null");
		}
		else
		{
			print("\n" + toHexStringAscii(b));
		}
	}
	
	
	public static void dump(String s, byte[] b)
	{
		if(b == null)
		{
			print(s, "null");
		}
		else
		{
			print(s, "\n" + toHexStringAscii(b));
		}
	}
	
	
	public static void describe(Object x)
	{
		print(Dump.describe(x));
	}
	
	
	public static void where(Object ... ss)
	{
		StackTraceElement[] tr = new Throwable().getStackTrace();
		int start = 1;
		int max = 5;
		boolean space = false;
		
		SB sb = new SB();
		if(ss.length > 0)
		{
			space = true;
			
			for(Object x: ss)
			{
				sb.a(String.valueOf(x));
				sb.a(' ');
			}
			
			sb.nl();
		}
		
		for(int i=0; i<max; i++)
		{
			int ix = start + i;
			if(ix >= tr.length)
			{
				break;
			}
			
			StackTraceElement t = tr[ix];
			if(space)
			{
				sb.a("  ");
			}
			else
			{
				space = true;
			}
			
			sb.a(getClassName(t) + "." + t.getMethodName() + " (" + t.getFileName() + ":" + t.getLineNumber() + ")\n");
		}
		System.out.println(sb);
	}
	
	
	public static void pp(Object a)
	{
		System.out.print(a == null ? "<null>" : a.toString());
	}
	
	
	public static void p(Object a)
	{
		System.out.println(a == null ? "<null>" : a.toString());
	}
	
	
	public static void p(Object ... a)
	{
		SB sb = new SB();
		for(Object x: a)
		{
			if(sb.length() > 0)
			{
				sb.append(" ");
			}
			sb.append(x);
		}
		System.out.println(sb.toString());
	}
}
