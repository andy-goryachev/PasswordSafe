// Copyright © 2010-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options;
import goryachev.common.util.CDateFormat;
import goryachev.common.util.CSettings;
import java.util.Collection;


public class TimeFormatOption
	extends COption<CDateFormat>
{
	public final static String FORMAT_12H = "h:mm a";
	public final static String FORMAT_12H_SECONDS = "h:mm:ss a";
	public final static String FORMAT_24H = "HH:mm";
	public final static String FORMAT_24H_SECONDS = "HH:mm:ss";
	

	public TimeFormatOption(String id, CSettings settings, Collection<COption<?>> list)
	{
		super(id, settings, list);
	}


	public TimeFormatOption(String id)
	{
		super(id);
	}


	public CDateFormat defaultValue()
	{
		return new CDateFormat(FORMAT_24H);
	}


	public CDateFormat parseProperty(String s)
	{
		return new CDateFormat(s);
	}


	public String toProperty(CDateFormat f)
	{
		return f.getPattern();
	}
}
