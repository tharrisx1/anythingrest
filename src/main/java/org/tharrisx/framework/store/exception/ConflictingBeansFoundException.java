package org.tharrisx.framework.store.exception;

import java.util.Map;

import org.tharrisx.framework.store.core.BeanStoreStackInfo;

/**
 * Thrown when a BeanStore is asked to retrieve a single item by a set of
 * property values as a unique key, and more than a single bean is found.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class ConflictingBeansFoundException extends BeanStoreException {

  private static final long serialVersionUID = 8635140302540236976L;

  public ConflictingBeansFoundException(BeanStoreStackInfo info, int count, String beanId) {
    super(info, count + " beans matched id: " + beanId);
  }

  public ConflictingBeansFoundException(BeanStoreStackInfo info, int count, Map<String, String> propertyValues) {
    super(info, count + " beans matched unique criteria: " + propertyValues);
  }
}
