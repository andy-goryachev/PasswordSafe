// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.i18n;
import java.util.Calendar;


/**
 * Calendar Localization.
 */
public class CalendarLocalization
{
	public static String getMonth(int month)
	{
		switch(month)
		{
		case  0: return TXT.get("CalendarTools.january", "January");
		case  1: return TXT.get("CalendarTools.february", "February");
		case  2: return TXT.get("CalendarTools.march", "March");
		case  3: return TXT.get("CalendarTools.april", "April");
		case  4: return TXT.get("CalendarTools.may", "May");
		case  5: return TXT.get("CalendarTools.june", "June");
		case  6: return TXT.get("CalendarTools.july", "July");
		case  7: return TXT.get("CalendarTools.august", "August");
		case  8: return TXT.get("CalendarTools.september", "September");
		case  9: return TXT.get("CalendarTools.october", "October");
		case 10: return TXT.get("CalendarTools.november", "November");
		case 11: return TXT.get("CalendarTools.december", "December");
		}
		return null;
	}
	
	
	public static String getShortMonth(int month)
	{
		switch(month)
		{
		case  0: return TXT.get("CalendarTools.short.january", "Jan");
		case  1: return TXT.get("CalendarTools.short.february", "Feb");
		case  2: return TXT.get("CalendarTools.short.march", "Mar");
		case  3: return TXT.get("CalendarTools.short.april", "Apr");
		case  4: return TXT.get("CalendarTools.short.may", "May");
		case  5: return TXT.get("CalendarTools.short.june", "Jun");
		case  6: return TXT.get("CalendarTools.short.july", "Jul");
		case  7: return TXT.get("CalendarTools.short.august", "Aug");
		case  8: return TXT.get("CalendarTools.short.september", "Sep");
		case  9: return TXT.get("CalendarTools.short.october", "Oct");
		case 10: return TXT.get("CalendarTools.short.november", "Nov");
		case 11: return TXT.get("CalendarTools.short.december", "Dec");
		}
		return null;
	}
	
	
	/** returns short day of week using Monday=0 system */
	public static final String getShortDayOfWeek(int d)
	{
		switch(d)
		{
		case 0: return TXT.get("CalendarTools.short.monday", "Mo");
		case 1: return TXT.get("CalendarTools.short.tuesday", "Tu");
		case 2: return TXT.get("CalendarTools.short.wednesday", "We");
		case 3: return TXT.get("CalendarTools.short.thursday", "Th");
		case 4: return TXT.get("CalendarTools.short.friday", "Fr");
		case 5: return TXT.get("CalendarTools.short.saturday", "Sa");
		case 6: return TXT.get("CalendarTools.short.sunday", "Su");
		}
		return null;
	}
	
	
	/** returns short day of week using Calendar.DAY_OF_WEEK numbers */
	public static final String getShortDayOfWeekCalendar(int d)
	{
		switch(d)
		{
		case Calendar.MONDAY: return getShortDayOfWeek(0);
		case Calendar.TUESDAY: return getShortDayOfWeek(1);
		case Calendar.WEDNESDAY: return getShortDayOfWeek(2);
		case Calendar.THURSDAY: return getShortDayOfWeek(3);
		case Calendar.FRIDAY: return getShortDayOfWeek(4);
		case Calendar.SATURDAY: return getShortDayOfWeek(5);
		case Calendar.SUNDAY: return getShortDayOfWeek(6);
		}
		return null;
	}
	
	
	public static final String getDayOfWeek(int d)
	{
		switch(d)
		{
		case 0: return TXT.get("CalendarTools.long.monday", "Monday");
		case 1: return TXT.get("CalendarTools.long.tuesday", "Tuesday");
		case 2: return TXT.get("CalendarTools.long.wednesday", "Wednesday");
		case 3: return TXT.get("CalendarTools.long.thursday", "Thursday");
		case 4: return TXT.get("CalendarTools.long.friday", "Friday");
		case 5: return TXT.get("CalendarTools.long.saturday", "Saturday");
		case 6: return TXT.get("CalendarTools.long.sunday", "Sunday");
		}
		return null;
	}
}
