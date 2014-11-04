package org.tharrisx.framework.callstats;

import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;

public class TimingMemento {

  private final long start;

  private long getStart() {
    return this.start;
  }

  public TimingMemento() {
    this.start = Utils.nanoTime();
  }

  public double getNanoDuration() {
    return Utils.nanoTime() - getStart();
  }
}
