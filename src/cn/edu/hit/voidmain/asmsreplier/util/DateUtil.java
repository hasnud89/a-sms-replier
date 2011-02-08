package cn.edu.hit.voidmain.asmsreplier.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {
	/**
	 * Get the string of the date using format "yyyy-MM-dd HH:mm:ss"
	 * @param date
	 * @return a string formatted from date
	 */
	public static String getDateString(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(date);
		return time;
	}
	
	/**
	 * Parse a string that contains date, return a date object
	 * using format "yyyy-MM-dd HH:mm:ss"
	 * @param dateString
	 * @return a date object that is parsed from dateString
	 */
	public static Date convertString2Date(String dateString)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
