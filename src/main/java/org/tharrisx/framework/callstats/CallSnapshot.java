package org.tharrisx.framework.callstats;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Contains a snapshot of Handler usage statistics.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@XStreamAlias("call-snapshot")
public class CallSnapshot extends Bean implements Comparable<CallSnapshot> {

  String name = null;

  public String getName() {
    return this.name;
  }

  String lastStatReset = null;

  public String getLastStatReset() {
    return this.lastStatReset;
  }

  String lastCallStamp = null;

  public String getLastCallStamp() {
    return this.lastCallStamp;
  }

  String lastCallDuration = null;

  public String getLastCallDuration() {
    return this.lastCallDuration;
  }

  String successCount = null;

  public String getSuccessCount() {
    return this.successCount;
  }

  String successTotalDuration = null;

  public String getSuccessTotalDuration() {
    return this.successTotalDuration;
  }

  String successMeanDuration = null;

  public String getSuccessMeanDuration() {
    return this.successMeanDuration;
  }

  String successMedianDuration = null;

  public String getSuccessMedianDuration() {
    return this.successMedianDuration;
  }

  String successMeanMedianDifference = null;

  public String getSuccessMeanMedianDifference() {
    return this.successMeanMedianDifference;
  }

  String successStandardDeviation = null;

  public String getSuccessStandardDeviation() {
    return this.successStandardDeviation;
  }

  String successVariation = null;

  public String getSuccessVariation() {
    return this.successVariation;
  }

  String failureCount = null;

  public String getFailureCount() {
    return this.failureCount;
  }

  String failureTotalDuration = null;

  public String getFailureTotalDuration() {
    return this.failureTotalDuration;
  }

  String failureMeanDuration = null;

  public String getFailureMeanDuration() {
    return this.failureMeanDuration;
  }

  String failureMedianDuration = null;

  public String getFailureMedianDuration() {
    return this.failureMedianDuration;
  }

  String failureMeanMedianDifference = null;

  public String getFailureMeanMedianDifference() {
    return this.failureMeanMedianDifference;
  }

  String failureStandardDeviation = null;

  public String getFailureStandardDeviation() {
    return this.failureStandardDeviation;
  }

  String failureVariation = null;

  public String getFailureVariation() {
    return this.failureVariation;
  }

  @Override
  public int compareTo(CallSnapshot o) {
    return this.name.compareTo(o.getName());
  }

  @Override
  public boolean equals(Object object) {
    if(!(object instanceof CallSnapshot))
      return false;
    CallSnapshot rhs = (CallSnapshot) object;
    return new EqualsBuilder().append(this.name, rhs.name).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(1346178609, -304132877).append(this.name).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("name", getName()).append("lastStatReset", getLastStatReset()).append("lastCallStamp", getLastCallStamp())
        .append("lastCallDuration", getLastCallDuration()).append("successCount", getSuccessCount()).append("successTotalDuration", getSuccessTotalDuration())
        .append("successMeanDuration", getSuccessMeanDuration()).append("successMedianDuration", getSuccessMedianDuration()).append("successMeanMedianDifference", getSuccessMeanMedianDifference())
        .append("successStandardDeviation", getSuccessStandardDeviation()).append("successVariation", getSuccessVariation()).append("failureCount", getFailureCount())
        .append("failureTotalDuration", getFailureTotalDuration()).append("failureMeanDuration", getFailureMeanDuration()).append("failureMedianDuration", getFailureMedianDuration())
        .append("failureMeanMedianDifference", getFailureMeanMedianDifference()).append("failureStandardDeviation", getFailureStandardDeviation()).append("failureVariation", getFailureVariation())
        .toString();
  }
}
