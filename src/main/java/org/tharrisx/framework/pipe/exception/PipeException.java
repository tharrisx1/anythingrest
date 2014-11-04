package org.tharrisx.framework.pipe.exception;

/**
 * Generic exception thrown from the Transport engine.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class PipeException extends RuntimeException {

  private static final long serialVersionUID = -7283094588196833878L;

  public PipeException(String arg0) {
    super(arg0);
  }

  public PipeException(Throwable arg0) {
    super(arg0);
  }

  public PipeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
