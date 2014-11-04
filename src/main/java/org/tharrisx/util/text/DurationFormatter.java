package org.tharrisx.util.text;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Utility class for the string formatting of temporal durations. The units
 * used will depend on the size of the duration value given.
 * 
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class DurationFormatter {

  private static NumberFormat TIME_NS_FORMAT = new DecimalFormat("#######0");
  private static NumberFormat TIME_MCS_FORMAT = new DecimalFormat("##0.0000");
  private static NumberFormat TIME_MS_FORMAT = new DecimalFormat("##0.0000");
  private static NumberFormat TIME_S_FORMAT = new DecimalFormat("###0.000");
  private static NumberFormat TIME_M_FORMAT = new DecimalFormat("###0.000");
  private static NumberFormat TIME_H_FORMAT = new DecimalFormat("###0.000");
  private static NumberFormat TIME_D_FORMAT = new DecimalFormat("###0.000");

  private static double TIMECONV_TO_MCS = 1000.0;
  private static double TIMECONV_TO_MS = TIMECONV_TO_MCS * 1000.0;
  private static double TIMECONV_TO_S = TIMECONV_TO_MS * 1000.0;
  private static double TIMECONV_TO_M = TIMECONV_TO_S * 60.0;
  private static double TIMECONV_TO_H = TIMECONV_TO_M * 60.0;
  private static double TIMECONV_TO_D = TIMECONV_TO_H * 24.0;

  public static String formatDuration(Number duration) {
    String ret = null;
    if(duration.doubleValue() < TIMECONV_TO_MCS) {
      ret = TIME_NS_FORMAT.format(duration.longValue()) + "ns";
    } else if(duration.doubleValue() < TIMECONV_TO_MS) {
      ret = TIME_MCS_FORMAT.format(duration.longValue() / TIMECONV_TO_MCS) + "\u00B5s";
    } else if(duration.doubleValue() < TIMECONV_TO_S) {
      ret = TIME_MS_FORMAT.format(duration.longValue() / TIMECONV_TO_MS) + "ms";
    } else if(duration.doubleValue() < TIMECONV_TO_M) {
      ret = TIME_S_FORMAT.format(duration.longValue() / TIMECONV_TO_S) + "s";
    } else if(duration.doubleValue() < TIMECONV_TO_H) {
      ret = TIME_M_FORMAT.format(duration.longValue() / TIMECONV_TO_M) + "m";
    } else if(duration.doubleValue() < TIMECONV_TO_D) {
      ret = TIME_H_FORMAT.format(duration.longValue() / TIMECONV_TO_H) + "h";
    } else {
      ret = TIME_D_FORMAT.format(duration.longValue() / TIMECONV_TO_D) + "d";
    }
    return ret;
  }
}
