package org.tharrisx.framework.store;

import org.tharrisx.framework.store.core.BeanStoreStackInfo;
import org.tharrisx.framework.store.exception.InvalidBeanPropertyValueException;
import org.tharrisx.framework.store.exception.NoSuchBeanPropertyException;

/**
 * Converts String values appropriate for HTTP into cast bean property values
 * for use in matching and criteria queries.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface BeanPropertyValueAdapter {

  /**
   * Converts from the HTTP appropriate String value into the bean property's
   * type.
   * 
   * @param info BeanStoreStackInfo The bean store stack
   * @param propertyName String The name of the bean property
   * @param propertyValue String The String value to convert from
   * @return Object The value returned now matchs the bean's property type
   * @throws NoSuchBeanPropertyException if something is wrong with the bean property type
   */
  Object getAdaptedValue(BeanStoreStackInfo info, String propertyName, String propertyValue) throws NoSuchBeanPropertyException, InvalidBeanPropertyValueException;

  /**
   * Converts to the String value
   * 
   * @param info BeanStoreStackInfo The bean store stack
   * @param propertyName String The name of the bean property
   * @param propertyValue String The String value to convert from
   * @return String The value returned is the String representation of the bean property value
   * @throws NoSuchBeanPropertyException if something is wrong with the bean property type
   */
  String getStringValue(BeanStoreStackInfo info, String propertyName, Object propertyValue) throws NoSuchBeanPropertyException, InvalidBeanPropertyValueException;
}
