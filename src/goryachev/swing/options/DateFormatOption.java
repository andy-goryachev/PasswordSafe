// Copyright © 2010-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options;
import goryachev.common.util.CDateFormat;
import goryachev.common.util.CSettings;
import java.util.Collection;


public class DateFormatOption
	extends COption<CDateFormat>
{
	public final static String FORMAT_DDMMYYYY_DASH = "dd-MM-yyyy";
	public final static String FORMAT_DDMMYYYY_DOT = "dd.MM.yyyy";
	public final static String FORMAT_DDMMYYYY_SLASH = "dd/MM/yyyy";
	public final static String FORMAT_MMDDYYYY_SLASH = "MM/dd/yyyy";
	public final static String FORMAT_YYYYMMDD_DASH = "yyyy-MM-dd";
	public final static String FORMAT_YYYYMMDD_DOT = "yyyy.MM.dd";
	

	public DateFormatOption(String id, CSettings settings, Collection<COption<?>> list)
	{
		super(id, settings, list);
	}


	public DateFormatOption(String id)
	{
		super(id);
	}


	public CDateFormat defaultValue()
	{
		return new CDateFormat(FORMAT_YYYYMMDD_DOT);
	}


	public CDateFormat parseProperty(String s)
	{
		return new CDateFormat(s);
	}


	public String toProperty(CDateFormat f)
	{
		return f == null ? null : f.getPattern();
	}
}
