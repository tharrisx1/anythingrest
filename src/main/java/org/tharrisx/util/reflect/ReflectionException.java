package org.tharrisx.util.reflect;

/**
 * An exception thrown when a reflection operation fails. This exists so that we
 * don't have exception indigestion.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class ReflectionException extends RuntimeException {

  private static final long serialVersionUID = 6267854025788998331L;

  public ReflectionException() {
    super();
  }

  public ReflectionException(String message) {
    super(message);
  }

  public ReflectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReflectionException(Throwable cause) {
    super(cause);
  }

  /**
   * To String!
   * 
   * @return String
   */
  @Override
  public String toString() {
    String ret = null;
    ret = "ReflectionException: Message='" + super.getMessage();
    if(null != super.getCause()) {
      ret += "' Cause='" + super.getCause().toString() + '\'';
    }
    return ret;
  }
}
