// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.common.util.CDateFormat;
import goryachev.common.util.CLookup;
import goryachev.i18n.TXT;
import goryachev.swing.options.DateFormatOption;
import java.text.SimpleDateFormat;


public class DateFormatOptionEditor
	extends ChoiceOptionEditor<CDateFormat>
{
	private CLookup lookup;
	
	
	public DateFormatOptionEditor(DateFormatOption op)
	{
		super(op);
		
		String d = TXT.get("DateFormatOptionEditor.abbreviated.day", "Day");
		String m = TXT.get("DateFormatOptionEditor.abbreviated.month", "Month");
		String y = TXT.get("DateFormatOptionEditor.abbreviated.year", "Year");
		
		Long t = System.currentTimeMillis();
		
		String dmyDash = d + "-" + m + "-" + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_DDMMYYYY_DASH).format(t) + ")";
		String dmyDot = d + "." + m + "." + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_DDMMYYYY_DOT).format(t) + ")";
		String dmySlash = d + "/" + m + "/" + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_DDMMYYYY_SLASH).format(t) + ")";
		String mdySlash = m + "/" + d + "/" + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_MMDDYYYY_SLASH).format(t) + ")";
		String ymdDash = y + "-" + m + "-" + d + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_YYYYMMDD_DASH).format(t) + ")";
		String ymdDot = y + "." + m + "." + d + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_YYYYMMDD_DOT).format(t) + ")";
		
		lookup = new CLookup
		(
			dmyDash,  DateFormatOption.FORMAT_DDMMYYYY_DASH,
			dmyDot,   DateFormatOption.FORMAT_DDMMYYYY_DOT,
			dmySlash, DateFormatOption.FORMAT_DDMMYYYY_SLASH,
			mdySlash, DateFormatOption.FORMAT_MMDDYYYY_SLASH,
			ymdDash,  DateFormatOption.FORMAT_YYYYMMDD_DASH,
			ymdDot,   DateFormatOption.FORMAT_YYYYMMDD_DOT
		);
		
		setChoices(new String[]
		{
			mdySlash,
			ymdDash,
			ymdDot,
			dmyDash,
			dmyDot,
			dmySlash
		});
	}


	protected CDateFormat parseEditorValue(String s)
	{
		String spec = (String)lookup.lookup(s);
		return spec == null ? null : new CDateFormat(spec);
	}


	protected String toEditorValue(CDateFormat f)
	{
		return f.getPattern();
	}
	
	
	public String getSearchString()
	{
		return null;
	}
	
	
	public void setEditorValue(CDateFormat f)
	{
		String v = (f == null ? null : f.getPattern());
		v = (String)lookup.lookup(v);
		combo.setSelectedItem(v);
	}
}
