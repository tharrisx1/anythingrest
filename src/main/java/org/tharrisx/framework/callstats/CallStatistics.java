package org.tharrisx.framework.callstats;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.tharrisx.util.Statistics;
import org.tharrisx.util.dateformat.SafeDateFormat;
import org.tharrisx.util.dateformat.SafeDateFormatFactory;
import org.tharrisx.util.text.DurationFormatter;

/**
 * Holds statistics on Handler usage.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class CallStatistics {

  private static SafeDateFormat SDF = SafeDateFormatFactory.get(SafeDateFormatFactory.DATETIME_US_SHORT);
  private static NumberFormat COUNT_FORMAT = new DecimalFormat("#######0");

  private String name = null;
  private Date lastStatReset = null;
  private Date lastCallStamp = null;
  private double lastCallDuration = 0;
  private final List<Double> successTimes = new LinkedList<>();
  private final List<Double> failureTimes = new LinkedList<>();

  CallStatistics(String theHandlerName) {
    this.name = theHandlerName;
    this.lastStatReset = new Date();
  }

  synchronized void resetStatistics() {
    this.lastCallStamp = null;
    this.lastStatReset = new Date();
    this.lastCallDuration = 0;
    this.successTimes.clear();
    this.failureTimes.clear();
  }

  synchronized void recordCall(boolean success, double duration) {
    if(success) {
      this.successTimes.add(Double.valueOf(duration));
    } else {
      this.failureTimes.add(Double.valueOf(duration));
    }
    this.lastCallStamp = new Date();
    this.lastCallDuration = duration;
  }

  synchronized CallSnapshot getSnapshot() {
    Double[] successArray = getListOfDoubleAsDoubleArray(this.successTimes);
    Double[] failureArray = getListOfDoubleAsDoubleArray(this.failureTimes);
    CallSnapshot ret = new CallSnapshot();
    ret.name = this.name;
    ret.lastStatReset = SDF.format(this.lastStatReset);
    ret.lastCallStamp = SDF.format(this.lastCallStamp);
    ret.lastCallDuration = DurationFormatter.formatDuration(this.lastCallDuration);
    ret.successCount = COUNT_FORMAT.format(this.successTimes.size());
    ret.successTotalDuration = DurationFormatter.formatDuration(Statistics.computeTotal(successArray));
    if(this.successTimes.size() > 1) {
      ret.successMeanDuration = DurationFormatter.formatDuration(Statistics.computeMean(successArray));
      ret.successMedianDuration = DurationFormatter.formatDuration(Statistics.computeMedian(successArray));
      ret.successMeanMedianDifference = DurationFormatter
          .formatDuration(new Double(Math.abs(Statistics.computeMean(successArray).doubleValue() - Statistics.computeMedian(successArray).doubleValue())));
      ret.successStandardDeviation = DurationFormatter.formatDuration(Statistics.computeStandardDeviation(successArray));
      ret.successVariation = DurationFormatter.formatDuration(Statistics.computeVariance(successArray));
    }
    ret.failureCount = COUNT_FORMAT.format(this.failureTimes.size());
    ret.failureTotalDuration = DurationFormatter.formatDuration(Statistics.computeTotal(failureArray));
    if(this.failureTimes.size() > 1) {
      ret.failureMeanDuration = DurationFormatter.formatDuration(Statistics.computeMean(failureArray));
      ret.failureMedianDuration = DurationFormatter.formatDuration(Statistics.computeMedian(failureArray));
      ret.failureMeanMedianDifference = DurationFormatter
          .formatDuration(new Double(Math.abs(Statistics.computeMean(failureArray).doubleValue() - Statistics.computeMedian(failureArray).doubleValue())));
      ret.failureStandardDeviation = DurationFormatter.formatDuration(Statistics.computeStandardDeviation(failureArray));
      ret.failureVariation = DurationFormatter.formatDuration(Statistics.computeVariance(failureArray));
    }
    return ret;
  }

  private static Double[] getListOfDoubleAsDoubleArray(List<Double> arg) {
    return arg.toArray(new Double[arg.size()]);
  }
}
