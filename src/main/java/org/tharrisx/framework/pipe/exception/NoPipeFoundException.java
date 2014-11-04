package org.tharrisx.framework.pipe.exception;

/**
 * Thrown from the Transport Engine when the requested Transport format is either not found or
 * is not valid in some way.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class NoPipeFoundException extends PipeException {

  private static final long serialVersionUID = 2216478329021797681L;

  public NoPipeFoundException(String arg0) {
    super(arg0);
  }

  public NoPipeFoundException(Throwable arg0) {
    super(arg0);
  }

  public NoPipeFoundException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
