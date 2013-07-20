package com.share.trade.common;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateUtil {

	public static Calendar getIndiaTime(){

		// Given a local time of 10am, get the time in Japan
		// Create a Calendar object with the local time zone
		Calendar local = new GregorianCalendar();
		local.set(Calendar.HOUR_OF_DAY, 10);               // 0..23
		local.set(Calendar.MINUTE, 0);
		local.set(Calendar.SECOND, 0);

		// Create an instance using Japan's time zone and set it with the local UTC
		Calendar japanCal = new GregorianCalendar(TimeZone.getTimeZone("Japan"));
		japanCal.setTimeInMillis(local.getTimeInMillis());

		// Get the foreign time
		int hour = japanCal.get(Calendar.HOUR);            // 3
		int minutes = japanCal.get(Calendar.MINUTE);       // 0
		int seconds = japanCal.get(Calendar.SECOND);       // 0
		boolean am = japanCal.get(Calendar.AM_PM) == Calendar.AM; //true


		// Given a time of 10am in Japan, get the local time
		japanCal = new GregorianCalendar(TimeZone.getTimeZone("Japan"));
		japanCal.set(Calendar.HOUR_OF_DAY, 10);            // 0..23
		japanCal.set(Calendar.MINUTE, 0);
		japanCal.set(Calendar.SECOND, 0);

		// Create a Calendar object with the local time zone and set
		// the UTC from japanCal
		local = new GregorianCalendar();
		local.setTimeInMillis(japanCal.getTimeInMillis());

		// Get the time in the local time zone
		hour = local.get(Calendar.HOUR);                   // 5
		minutes = local.get(Calendar.MINUTE);              // 0
		seconds = local.get(Calendar.SECOND);              // 0
		am = local.get(Calendar.AM_PM) == Calendar.AM;     // false
		
		return null;
	}
	
}
