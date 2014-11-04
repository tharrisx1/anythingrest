package org.tharrisx.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.tharrisx.util.dateformat.SafeDateFormat;
import org.tharrisx.util.dateformat.SafeDateFormatImpl;

/**
 * Date manipulation utilities
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class DateUtils {

  private static double MILLISECONDS_PER_YEAR = 1000 * 60 * 60 * 24 * 365.25;
  private static final String DATE_FORMAT_TIMEZONE = "yyyy-MM-dd'T'kk:mm:ss z";
  private static final String DATE_FORMAT_23H_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssZ";
  private static final SafeDateFormat DF_23H_TIMEZONE = new SafeDateFormatImpl(DATE_FORMAT_23H_TIMEZONE, Locale.US);
  private static final SafeDateFormat DF_TIMEZONE = new SafeDateFormatImpl(DATE_FORMAT_TIMEZONE, Locale.US);
  private static final String GMT = "GMT";

  public static double dateDifferenceInYears(Date arg1, Date arg2) {
    return dateDifferenceInMilliseconds(arg1, arg2) / MILLISECONDS_PER_YEAR;
  }

  /**
   * Returns the difference in milliseconds betwen the two dates. NOTE: These
   * two dates should be in the same timezone, or there will be a discrepancy.
   * 
   * @param arg1 Date
   * @param arg2 Date
   * @return long Absolute difference between the two dates, in milliseconds
   */
  public static long dateDifferenceInMilliseconds(Date arg1, Date arg2) {
    return Math.abs(arg1.getTime() - arg2.getTime());
  }

  public static String getStringFromDate(Date arg) {
    String ret = null;
    if(null != arg)
      ret = DateUtils.getStringFromCalendar(DateUtils.getCalendarFromDate(arg));
    return ret;
  }

  public static Date getDateFromString(String arg) throws ParseException {
    Date ret = null;
    if(null != arg)
      ret = DateUtils.getDateFromCalendar(DateUtils.getCalendarFromString(arg));
    return ret;
  }

  public static Date getDateFromCalendar(Calendar arg) {
    return arg.getTime();
  }

  public static Calendar getCalendarFromDate(Date arg) {
    Calendar ret = Calendar.getInstance();
    ret.setTime(arg);
    return ret;
  }

  public static Calendar getCalendarFromString(String dateString) throws ParseException {
    String localDateString = dateString;
    int plusSign = localDateString.lastIndexOf('+');
    int minusSign = localDateString.lastIndexOf('-');
    String timeZoneString = null;
    if(plusSign > minusSign) {
      timeZoneString = localDateString.substring(plusSign);
      localDateString = localDateString.substring(0, plusSign);
    } else {
      timeZoneString = localDateString.substring(minusSign);
      localDateString = localDateString.substring(0, minusSign);
    }
    Calendar calendar = new GregorianCalendar();
    TimeZone zone = TimeZone.getTimeZone(GMT + timeZoneString);
    calendar.setTimeZone(zone);
    calendar.setTime(DF_TIMEZONE.parse(localDateString + " " + GMT + timeZoneString));
    return calendar;
  }

  public static String getStringFromCalendar(Calendar calendar) {
    DF_23H_TIMEZONE.setTimeZone(calendar.getTimeZone());
    String dateStr = DF_23H_TIMEZONE.format(calendar.getTime());
    String returnStr = dateStr.substring(0, dateStr.length() - 2) + ":" + dateStr.substring(dateStr.length() - 2);
    return returnStr;
  }

  private DateUtils() {
    // private empty constructor for static utility class
  }
}
