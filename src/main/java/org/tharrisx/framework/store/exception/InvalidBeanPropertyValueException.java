package org.tharrisx.framework.store.exception;

import org.tharrisx.framework.store.core.BeanStoreStackInfo;

/**
 * Thrown when a BeanStore is asked to match bean properties against a set the user
 * provided, but either:
 * - One or more of those property names do not exist on the bean type
 * - One or more of the provided property values is not valid
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class InvalidBeanPropertyValueException extends BeanStoreException {

  private static final long serialVersionUID = 1502902639008068730L;

  public InvalidBeanPropertyValueException(BeanStoreStackInfo info, Throwable cause) {
    super(info, cause);
  }
}
