package org.tharrisx.framework.store.exception;

import org.tharrisx.framework.store.core.BeanStoreStackInfo;

/**
 * General exception for the BeanStore.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class BeanStoreException extends RuntimeException {

  private static final long serialVersionUID = -4069386873542239596L;

  private static String makeMessage(BeanStoreStackInfo info, String message) {
    StringBuilder sb = new StringBuilder();
    sb.append("Bean store (").append(info.getBeanType().getName()).append(") failure");
    if(null != info.getTransaction()) {
      sb.append(" transaction: ").append(info.getTransaction().getTransactionName());
    }
    if(null != info.getBeanStoreMethod()) {
      sb.append(" beanStoreMethod: ").append(info.getBeanStoreMethod());
    }
    if(null != message) {
      sb.append(": ").append(message);
    }
    return sb.toString();
  }

  public BeanStoreException(BeanStoreStackInfo info, String message) {
    super(makeMessage(info, message));
  }

  public BeanStoreException(BeanStoreStackInfo info, Throwable cause) {
    super(makeMessage(info, cause.getMessage()), cause);
  }

  public BeanStoreException(BeanStoreStackInfo info, String message, Throwable cause) {
    super(makeMessage(info, message), cause);
  }
}
