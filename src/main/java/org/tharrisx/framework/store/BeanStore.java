package org.tharrisx.framework.store;

import java.util.Map;

import org.tharrisx.framework.bean.BeanList;
import org.tharrisx.framework.bean.PageableBeanList;
import org.tharrisx.framework.store.exception.BeanStoreException;
import org.tharrisx.framework.store.exception.ConflictingBeansFoundException;
import org.tharrisx.framework.store.exception.NoBeanFoundException;

/**
 * Defines an API for a BeanStore, abstracting the persistence mechanism. This class is generic, and is parameterized
 * by the bean type. Beans to be used with a BeanStore must extend from StorableBean.
 * 
 * @param <T extends StorableBean>
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface BeanStore<T extends StorableBean> {

  // method names for tracking
  String METHOD_HAS_BEAN = "hasBean";
  String METHOD_HAS_BEAN_BY_UNIQUE_KEY = "hasBeanByUniqueKey";
  String METHOD_GET_BEAN = "getBean";
  String METHOD_GET_BEAN_BY_UNIQUE_KEY = "getBeanByUniqueKey";
  String METHOD_GET_ALL_MATCHING_BEANS = "getAllMatchingBeans";
  String METHOD_GET_ALL_BEANS_VIA_QUERY = "getAllBeansViaQuery";
  String METHOD_GET_PAGE_OF_MATCHING_BEANS = "getPageOfMatchingBeans";
  String METHOD_GET_PAGE_OF_BEANS_VIA_QUERY = "getPageOfBeansViaQuery";
  String METHOD_CREATE_OR_UPDATE_BEAN = "createOrUpdateBean";
  String METHOD_DELETE_BEAN = "deleteBean";

  /**
   * Returns true if and only if the beanId matches an existing bean.
   * 
   * @param beanId String
   * @return boolean 
   * @throws BeanStoreException
   */
  boolean hasBean(String beanId) throws BeanStoreException;

  /**
   * Returns true if and only if the property values match a unique bean.
   * 
   * @param propertyValues Map<String, String> Map of bean property names to String values
   * @return boolean
   * @throws ConflictingBeansFoundException
   * @throws BeanStoreException
   */
  boolean hasBeanByUniqueKey(Map<String, String> propertyValues) throws ConflictingBeansFoundException, BeanStoreException;

  /**
   * Retrieve a bean by its beanId
   * 
   * @param beanId String
   * @return T The bean
   * @throws NoBeanFoundException
   * @throws BeanStoreException
   */
  T getBean(String beanId) throws NoBeanFoundException, BeanStoreException;

  /**
   * Retrieve a bean by matching property values as a unique key.
   * 
   * @param propertyValues Map<String, String> Map of bean property names to String values
   * @return T The bean
   * @throws NoBeanFoundException
   * @throws ConflictingBeansFoundException
   * @throws BeanStoreException
   */
  T getBeanByUniqueKey(Map<String, String> propertyValues) throws NoBeanFoundException, ConflictingBeansFoundException, BeanStoreException;

  /**
   * Get the complete list of beans that match the property values.
   * 
   * @param propertyValues Map<String, String> Map of bean property names to String values
   * @return BeanList<T>
   * @throws BeanStoreException
   */
  BeanList<T> getAllMatchingBeans(Map<String, String> propertyValues) throws BeanStoreException;

  /**
   * Get the complete list of beans via a 'Named Query'.
   * 
   * @param queryName String The name of the 'Named Query', whatever that that may be...
   * @param queryParameters Object... The values to inject into the 'Named Query'.
   * @return BeanList<T>
   * @throws BeanStoreException
   */
  BeanList<T> getAllBeansViaQuery(String queryName, Object... queryParameters) throws BeanStoreException;

  /**
   * Get the pageable list of beans that match the property values.
   * 
   * @param start int The index into the full results at which to start the page
   * @param end int The index into the full results at which to end the page
   * @param propertyValues Map<String, String> Map of bean property names to String values
   * @return ExamplePageableBeanList<T>
   * @throws BeanStoreException
   */
  PageableBeanList<T> getPageOfMatchingBeans(int start, int end, String sortBy, String sortDirection, Map<String, String> propertyValues) throws BeanStoreException;

  /**
   * Get the pageable list of beans via a 'Named Query'.
   * 
   * @param start int The index into the full results at which to start the page
   * @param end int The index into the full results at which to end the page
   * @param queryName String The name of the 'Named Query', whatever that that may be...
   * @param queryParameters Object... The values to inject into the 'Named Query'.
   * @return ExamplePageableBeanList<T>
   * @throws BeanStoreException
   */
  PageableBeanList<T> getPageOfBeansViaQuery(int start, int end, String sortBy, String sortDirection, String queryName, Object... queryParameters) throws BeanStoreException;

  /**
   * Create or update a bean in the BeanStore.
   * 
   * @param bean T The bean to store
   * @return T The newly persisted bean, rehydrated for inspection
   * @throws BeanStoreException
   */
  T createOrUpdateBean(T bean, BeanStoreTransaction transaction) throws BeanStoreException;

  /**
   * Delete the bean from the BeanStore.
   * 
   * @param beanId The id of the bean
   * @return boolean true if successful
   * @throws NoBeanFoundException
   * @throws BeanStoreException
   */
  boolean deleteBean(String beanId, BeanStoreTransaction transaction) throws NoBeanFoundException, BeanStoreException;
}
