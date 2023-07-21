// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.common.util.CDateFormat;
import goryachev.common.util.CLookup;
import goryachev.swing.options.TimeFormatOption;
import java.text.SimpleDateFormat;


public class TimeFormatOptionEditor
	extends ChoiceOptionEditor<CDateFormat>
{
	private CLookup lookup;
	
	
	public TimeFormatOptionEditor(TimeFormatOption op)
	{
		super(op);
		
		Long t = System.currentTimeMillis();
		
		String time12 = new SimpleDateFormat(TimeFormatOption.FORMAT_12H).format(t);
		String time12s = new SimpleDateFormat(TimeFormatOption.FORMAT_12H_SECONDS).format(t);
		String time24 = new SimpleDateFormat(TimeFormatOption.FORMAT_24H).format(t);
		String time24s = new SimpleDateFormat(TimeFormatOption.FORMAT_24H_SECONDS).format(t);
		
		lookup = new CLookup
		(
			time12,  TimeFormatOption.FORMAT_12H,
			time12s, TimeFormatOption.FORMAT_12H_SECONDS,
			time24,  TimeFormatOption.FORMAT_24H,
			time24s, TimeFormatOption.FORMAT_24H_SECONDS
		);
		
		setChoices(new String[]
		{
			time24,
			time24s,
			time12,
			time12s
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
}
