package org.tharrisx.util;

import java.util.Arrays;

/**
 * Compute statistics.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class Statistics {

  public static Number computeTotal(Number[] values) {
    double sum = 0;
    for(int cv = 0; cv < values.length; cv++)
      sum += values[cv].doubleValue();
    return new Double(sum);
  }

  public static Number computeAverage(Number[] values) {
    double sum = 0;
    for(int cv = 0; cv < values.length; cv++)
      sum += values[cv].doubleValue();
    return new Double(sum / values.length);
  }

  public static Number computeMedian(Number[] values) {
    double ret = 0.0;
    Number[] b = new Number[values.length];
    System.arraycopy(values, 0, b, 0, b.length);
    Arrays.sort(b);
    if(values.length % 2 == 0) {
      ret = (b[(b.length / 2) - 1].doubleValue() + b[b.length / 2].doubleValue()) / 2.0;
    } else {
      ret = b[b.length / 2].doubleValue();
    }
    return new Double(ret);
  }

  public static Number computeMode(Number[] values) {
    Number maxValue = new Double(0.0);
    int maxCount = 0;
    for(int i = 0; i < values.length; ++i) {
      int count = 0;
      for(int j = 0; j < values.length; j++)
        if(values[j] == values[i])
          ++count;
      if(count > maxCount) {
        maxCount = count;
        maxValue = values[i];
      }
    }
    return maxValue;
  }

  public static Number computeMean(Number[] values) {
    return computeMean(values, 0, values.length);
  }

  @SuppressWarnings("all")
  private static Number computeMean(Number[] values, int offset, int count) {
    if(count < 1) {
      throw new IllegalArgumentException("The number of values to process must be one or larger.");
    }
    double sum = 0.0;
    final int UNTIL = offset + count;
    do {
      sum += values[offset++].doubleValue();
    } while(offset != UNTIL);
    return new Double(sum / count);
  }

  public static Number computeStandardDeviation(Number[] values) {
    return computeStandardDeviation(values, 0, values.length);
  }

  private static Number computeStandardDeviation(Number[] values, int offset, int number) {
    Number mean = computeMean(values, offset, number);
    return computeStandardDeviation(values, 0, values.length, mean);
  }

  private static Number computeStandardDeviation(Number[] values, int offset, int number, Number mean) {
    return new Double(Math.sqrt(computeVariance(values, offset, number, mean).doubleValue()));
  }

  public static Number computeVariance(final Number[] values) {
    return computeVariance(values, 0, values.length);
  }

  private static Number computeVariance(final Number[] values, int offset, final int number) {
    Number mean = computeMean(values, offset, number);
    return computeVariance(values, 0, values.length, mean);
  }

  @SuppressWarnings("all")
  private static Number computeVariance(final Number[] values, int offset, final int number, final Number mean) {
    if(number < 2) {
      throw new IllegalArgumentException("The number of values to process must be two or larger.");
    }
    double sum = 0.0;
    final int UNTIL = offset + number;
    do {
      double diff = values[offset++].doubleValue() - mean.doubleValue();
      sum += diff * diff;
    } while(offset != UNTIL);
    return new Double(sum / (number - 1));
  }

  private Statistics() {
    // private default constructor for utility class
  }
}
