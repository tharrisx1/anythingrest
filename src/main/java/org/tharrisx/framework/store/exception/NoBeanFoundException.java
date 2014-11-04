package org.tharrisx.framework.store.exception;

import java.util.Map;

import org.tharrisx.framework.store.core.BeanStoreStackInfo;

/**
 * Thrown when a BeanStore is asked to retrieve a single item, and it is not
 * found when expected.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class NoBeanFoundException extends BeanStoreException {

  private static final long serialVersionUID = -5787338778405613300L;

  public NoBeanFoundException(BeanStoreStackInfo info, String beanId) {
    super(info, "No beans matched id: " + beanId);
  }

  public NoBeanFoundException(BeanStoreStackInfo info, Map<String, String> propertyValues) {
    super(info, "No beans matched unique criteria: " + propertyValues);
  }
}
