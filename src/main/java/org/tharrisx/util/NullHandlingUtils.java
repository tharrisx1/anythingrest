package org.tharrisx.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Provides utility methods to utilize primitive wrappers as if they were
 * primitives. This may no longer be needed once we move to Java 1.5, since it
 * has auto-boxing.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class NullHandlingUtils {

  public static Integer nullSafe(Integer arg, int valueIfNull) {
    return null != arg ? arg : Integer.valueOf(valueIfNull);
  }

  public static Long nullSafe(Long arg, long valueIfNull) {
    return null != arg ? arg : Long.valueOf(valueIfNull);
  }

  public static Float nullSafe(Float arg, float valueIfNull) {
    return null != arg ? arg : Float.valueOf(valueIfNull);
  }

  public static Double nullSafe(Double arg, double valueIfNull) {
    return null != arg ? arg : Double.valueOf(valueIfNull);
  }

  public static Date nullSafeClone(Date arg) {
    return null == arg ? null : (Date) arg.clone();
  }

  public static Calendar nullSafeClone(Calendar arg) {
    return null == arg ? null : (Calendar) arg.clone();
  }

  private NullHandlingUtils() {
    /* private constructor for utility class */
  }
}
