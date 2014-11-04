package org.tharrisx.framework.pipe.exception;

/**
 * Thrown from the Transport Engine when the Bean type being registered is not
 * valid in some way.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class InvalidBeanTypeException extends PipeException {

  private static final long serialVersionUID = 3284846334698134796L;

  public InvalidBeanTypeException(String arg0) {
    super(arg0);
  }

  public InvalidBeanTypeException(Throwable arg0) {
    super(arg0);
  }

  public InvalidBeanTypeException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }
}
