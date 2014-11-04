package org.tharrisx.framework.pipe.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.pipe.PipeContext;
import org.tharrisx.framework.pipe.ProtectionType;
import org.tharrisx.framework.pipe.annotations.PipeProtection;

/**
 * Hides PipeManagerCore internal details from "prying eyes". Never instantiate this class directly,
 * always use Bean or one of its subclasses as your base.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class PipeableBean {

  /**
   * This property serves as a cheat, to ferry the pipe context of the call being made back to our
   * custom ReflectionProviderWrapper, which implements the security layer deep inside the XStream
   * library.
   * This defaults to PipeContext.UNMARSHAL because in that case, XStream is creating the object,
   * not our code, so its natural to default to it.
   */
  @PipeProtection(ProtectionType.NAME_HIDDEN)
  private PipeContext pipeContext = PipeContext.UNMARSHAL;

  PipeContext getPipeContext() {
    return this.pipeContext;
  }

  void setPipeContext(PipeContext arg) {
    this.pipeContext = arg;
  }

  public PipeableBean() {
    // public default constructor
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("pipeContext", getPipeContext()).toString();
  }
}
