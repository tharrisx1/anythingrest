package org.tharrisx.framework.store;

import org.tharrisx.framework.store.exception.BeanStoreException;

/**
 * Defines an API for a BeanStore factory so we all can get at these things
 * without knowing how it all happens. This also provides transaction
 * facilities so that transactions can span multiple bean types.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface BeanStoreFactory {

  BeanPropertyValueAdapter getBeanPropertyValueAdapter();

  /**
   * Provide the appropriately implemented BeanStore for the requested bean type.
   * 
   * @param <T extends StorableBean>
   * @param beanType Class<T>
   * @return BeanStore<T>
   */
  <T extends StorableBean> BeanStore<T> getBeanStore(Class<T> beanType);

  /**
   * Begin a database read/write transaction.
   * 
   * @param <T extends StorableBean>
   * @param beanType Class<T>
   * @param transactionName String
   * @return BeanStoreTransaction
   * @throws BeanStoreException
   */
  <T extends StorableBean> BeanStoreTransaction beginTransaction(Class<T> beanType, String transactionName) throws BeanStoreException;

  /**
   * End a database read/write transaction.
   * 
   * @param <T extends StorableBean>
   * @param beanType Class<T>
   * @param transaction BeanStoreTransaction
   * @throws BeanStoreException
   */
  <T extends StorableBean> void endTransaction(Class<T> beanType, BeanStoreTransaction transaction) throws BeanStoreException;
}
