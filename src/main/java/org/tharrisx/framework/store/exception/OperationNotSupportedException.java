package org.tharrisx.framework.store.exception;

import org.tharrisx.framework.store.core.BeanStoreStackInfo;

/**
 * Thrown when a BeanStore is asked to perform an operation, but this BeanStore
 * implementation does not support the operation.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class OperationNotSupportedException extends BeanStoreException {

  private static final long serialVersionUID = -1671503187752650649L;

  public OperationNotSupportedException(BeanStoreStackInfo info, Throwable cause) {
    super(info, cause);
  }

  public OperationNotSupportedException(BeanStoreStackInfo info, String message) {
    super(info, message);
  }
}
