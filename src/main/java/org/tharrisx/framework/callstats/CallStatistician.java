package org.tharrisx.framework.callstats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.util.log.Log;
import org.tharrisx.util.report.SimpleReportGenerator;
import org.tharrisx.util.text.DurationFormatter;
import org.tharrisx.util.text.StringUtils;

public class CallStatistician {

  public static class Snapshots {

    List<CallSnapshot> callSnapshots = null;

    public List<CallSnapshot> getSnapshots() {
      return this.callSnapshots;
    }

    Snapshots(List<CallSnapshot> theSnapshots) {
      this.callSnapshots = theSnapshots;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("callSnapshots", getSnapshots()).toString();
    }
  }

  private final Map<String, CallStatistics> callStatisticsMap = new HashMap<>();

  private final boolean callStatisticsEnabled;

  public boolean isCallStatisticsEnabled() {
    return this.callStatisticsEnabled;
  }

  public CallStatistician(final boolean enableCallStatistics1) {
    this.callStatisticsEnabled = enableCallStatistics1;
  }

  /**
   * Record the completion of a call.
   * 
   * @param callName String
   * @param success boolean
   * @param timing TimingMemento
   */
  public void recordCall(String callName, boolean success, TimingMemento timing) {
    if(!isCallStatisticsEnabled())
      return;
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "recordCall", callName, success, timing);
    try {
      double duration = timing.getNanoDuration();
      get(callName).recordCall(success, duration);
      if(Log.isInfoEnabled(getClass()))
        Log.info(getClass(), "recordCall", "Call " + callName + " " + (success ? "succeeded" : "failed") + ". Took " + DurationFormatter.formatDuration(duration));
      // $$$ temporary, once every X calls, a call stats report is logged. 
      if(this.reportCounter > REPORT_COUNTER_MAX) {
        if(Log.isInfoEnabled(getClass()))
          Log.info(getClass(), "recordCall", getSimpleCallSnapshotReport());
        this.reportCounter = 0;
      } else {
        this.reportCounter++;
      }
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "recordCall");
    }
  }

  private static final int REPORT_COUNTER_MAX = 100;
  private volatile int reportCounter = 0;

  public List<CallSnapshot> getSnapshots() {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "getSnapshots");
    List<CallSnapshot> ret = null;
    try {
      ret = new LinkedList<>();
      Iterator<CallStatistics> itr = this.callStatisticsMap.values().iterator();
      while(itr.hasNext())
        ret.add(itr.next().getSnapshot());
      Collections.sort(ret);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "getSnapshots", ret);
    }
  }

  public String getSimpleCallSnapshotReport() {
    StringBuilder retBuf = new StringBuilder();

    List<CallSnapshot> snapshots = getSnapshots();

    String[] headers = new String[] { "Call", "Last Reset", "Last Called", "Last Took", "+Count", "+Total", "+Mean", "+Median", "+<Diff", "+Std.Dev.", "+Variance", "-Count", "-Total", "-Mean",
        "-Median", "-<Diff", "-Std.Dev.", "-Variance" };
    String[][] reportData = new String[snapshots.size()][];
    int cv = 0;
    for(CallSnapshot snapshot : snapshots) {
      reportData[cv++] = new String[] { snapshot.getName(), snapshot.getLastStatReset(), snapshot.getLastCallStamp(), snapshot.getLastCallDuration(), snapshot.getSuccessCount(),
          snapshot.getSuccessTotalDuration(), snapshot.getSuccessMeanDuration(), snapshot.getSuccessMedianDuration(), snapshot.getSuccessMeanMedianDifference(),
          snapshot.getSuccessStandardDeviation(), snapshot.getSuccessVariation(), snapshot.getFailureCount(), snapshot.getFailureTotalDuration(), snapshot.getFailureMeanDuration(),
          snapshot.getFailureMedianDuration(), snapshot.getFailureMeanMedianDifference(), snapshot.getFailureStandardDeviation(), snapshot.getFailureVariation() };
    }
    retBuf.append(StringUtils.EOL_SYSTEM);
    retBuf.append("-- Call Statistics Report --");
    retBuf.append(new SimpleReportGenerator().getReport(headers, reportData));
    return retBuf.toString();
  }

  private CallStatistics get(String callName) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "get", callName);
    CallStatistics ret = null;
    try {
      ret = this.callStatisticsMap.get(callName);
      if(null == ret) {
        ret = new CallStatistics(callName);
        this.callStatisticsMap.put(callName, ret);
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "get", ret);
    }
  }
}
