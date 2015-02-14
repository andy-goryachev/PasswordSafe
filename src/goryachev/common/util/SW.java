// Copyright (c) 2005-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** SW measures elapsed time since instantioation or reset() */
public class SW
{
	public static final String ZEROES = "0000000000";
	private long start;


	public SW()
	{
		reset();
	}


	public void reset()
	{
		start = System.nanoTime();
	}


	/** returns elapsed time in nanoseconds */
	public long getElapsedTimeNano()
	{
		return System.nanoTime() - start;
	}


	/** returns elapsed time in milliseconds */
	public long getElapsedTimeMilli()
	{
		return getElapsedTimeNano() / 1000000;
	}


	protected static void append(SB sb, int n, int precision)
	{
		String s = String.valueOf(n);
		n = precision - s.length();
		if(n > 0)
		{
			sb.append(ZEROES, 0, n);
		}
		sb.append(s);
	}


	public String toString()
	{
		long elapsed = getElapsedTimeNano();
		return formatNanos(elapsed);
	}


	public static String formatNanos(long elapsed)
	{
		return formatMillis(elapsed / 1000000L);
	}
	

	public static String formatMillis(long elapsed)
	{
		boolean force = false;
		SB sb = new SB();

		int d = (int)(elapsed / CKit.MS_IN_A_DAY);
		if(d != 0)
		{
			sb.append(d);
			sb.append('-');
			elapsed %= CKit.MS_IN_A_DAY;
			force = true;
		}

		int h = (int)(elapsed / CKit.MS_IN_AN_HOUR);
		if(force || (h != 0))
		{
			append(sb, h, 2);
			sb.append(':');
			elapsed %= CKit.MS_IN_AN_HOUR;
			force = true;
		}

		int m = (int)(elapsed / CKit.MS_IN_A_MINUTE);
		if(force || (m != 0))
		{
			append(sb, m, 2);
			sb.append(':');
			elapsed %= CKit.MS_IN_A_MINUTE;
			force = true;
		}

		int s = (int)(elapsed / CKit.MS_IN_A_SECOND);
		if(force)
		{
			append(sb, s, 2);
		}
		else
		{
			sb.append(s);
		}
		sb.append('.');

		int ms = (int)(elapsed % CKit.MS_IN_A_SECOND);
		append(sb, ms, 3);

		return sb.toString();
	}
}
