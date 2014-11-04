package org.tharrisx.util.dateformat;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A caching factory for Sun's predefined safe date formatters.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class SafeDateFormatFactory {

  private static class SafeDateFormatKey {

    private String pattern = null;

    private String getPattern() {
      return this.pattern;
    }

    private Locale locale = null;

    private Locale getLocale() {
      return this.locale;
    }

    SafeDateFormatKey(String aPattern, Locale aLocale) {
      this.pattern = aPattern;
      this.locale = aLocale;
    }

    @Override
    public boolean equals(final Object other) {
      if(!(other instanceof SafeDateFormatKey))
        return false;
      SafeDateFormatKey castOther = (SafeDateFormatKey) other;
      return (getPattern() + getLocale()).equals(castOther.getPattern() + castOther.getLocale());
    }

    @Override
    public int hashCode() {
      return (getPattern() + getLocale()).hashCode();
    }
  }

  public static final String DATETIME_US_SHORT = "DATETIME_SHORT_US";
  public static final String DATE_SHORT_US = "DATE_SHORT_US";
  public static final String DATE_MEDIUM_US = "DATE_MEDIUM_US";
  public static final String DATE_LONG_US = "DATE_LONG_US";
  public static final String DATE_FULL_US = "DATE_FULL_US";
  public static final String TIME_SHORT_US = "TIME_SHORT_US";
  public static final String TIME_MEDIUM_US = "TIME_MEDIUM_US";
  public static final String TIME_LONG_US = "TIME_LONG_US";
  public static final String TIME_FULL_US = "TIME_FULL_US";

  private static final Map<SafeDateFormatKey, SafeDateFormat> SDF_CACHE = new HashMap<>();
  private static final Object SDF_CACHE_LOCK = new Object();

  static {
    synchronized(SDF_CACHE_LOCK) {
      SDF_CACHE.put(new SafeDateFormatKey(DATETIME_US_SHORT, Locale.US), new SafeDateFormatImpl(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)));
      SDF_CACHE.put(new SafeDateFormatKey(DATE_SHORT_US, Locale.US), new SafeDateFormatImpl(DateFormat.getDateInstance(DateFormat.SHORT)));
      SDF_CACHE.put(new SafeDateFormatKey(DATE_MEDIUM_US, Locale.US), new SafeDateFormatImpl(DateFormat.getDateInstance(DateFormat.MEDIUM)));
      SDF_CACHE.put(new SafeDateFormatKey(DATE_LONG_US, Locale.US), new SafeDateFormatImpl(DateFormat.getDateInstance(DateFormat.LONG)));
      SDF_CACHE.put(new SafeDateFormatKey(DATE_FULL_US, Locale.US), new SafeDateFormatImpl(DateFormat.getDateInstance(DateFormat.FULL)));
      SDF_CACHE.put(new SafeDateFormatKey(TIME_SHORT_US, Locale.US), new SafeDateFormatImpl(DateFormat.getTimeInstance(DateFormat.SHORT)));
      SDF_CACHE.put(new SafeDateFormatKey(TIME_MEDIUM_US, Locale.US), new SafeDateFormatImpl(DateFormat.getTimeInstance(DateFormat.MEDIUM)));
      SDF_CACHE.put(new SafeDateFormatKey(TIME_LONG_US, Locale.US), new SafeDateFormatImpl(DateFormat.getTimeInstance(DateFormat.LONG)));
      SDF_CACHE.put(new SafeDateFormatKey(TIME_FULL_US, Locale.US), new SafeDateFormatImpl(DateFormat.getTimeInstance(DateFormat.FULL)));
    }
  }

  public static SafeDateFormat get(String pattern) {
    SafeDateFormat ret = null;
    ret = get(pattern, Locale.US);
    return ret;
  }

  public static SafeDateFormat get(String pattern, Locale locale) {
    SafeDateFormat ret = null;
    synchronized(SDF_CACHE_LOCK) {
      SafeDateFormatKey key = new SafeDateFormatKey(pattern, locale);
      ret = SDF_CACHE.get(key);
      if(null == ret) {
        ret = new SafeDateFormatImpl(pattern, locale);
        SDF_CACHE.put(key, ret);
      }
      return ret;
    }
  }

  private SafeDateFormatFactory() {
    // private default constructor for static utility class
  }
}
