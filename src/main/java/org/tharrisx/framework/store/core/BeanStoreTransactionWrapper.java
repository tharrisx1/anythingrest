package org.tharrisx.framework.store.core;

import org.tharrisx.framework.store.BeanPropertyValueAdapter;
import org.tharrisx.framework.store.BeanStoreFactory;
import org.tharrisx.framework.store.BeanStoreTransaction;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.framework.store.exception.BeanStoreException;
import org.tharrisx.util.log.Log;

/**
 * A transaction wrapper - a simple procedural template that begins and commits a single bean store
 * method call. You can subclass this anonymously for some really tight code. Or subclass with
 * additional common implementation code, then subclass that anonymously.
 * 
 * This class is meant to be used by BeanStore implementations.
 * 
 * @param <U> The class parameter specifies the return type, if any, from the storage call. If none,
 * specify Object, and return a null value that you neglect to read.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class BeanStoreTransactionWrapper<U> {

  public final static String IMPLICIT_TRANSACTION_NAME = "implicit";

  // properties

  private final BeanStoreFactory beanStoreFactory;

  BeanStoreFactory getBeanStoreFactory() {
    return this.beanStoreFactory;
  }

  private final Class<? extends StorableBean> beanType;

  Class<? extends StorableBean> getBeanType() {
    return this.beanType;
  }

  private final String beanStoreMethod;

  String getBeanStoreMethod() {
    return this.beanStoreMethod;
  }

  /**
   * Must call this constructor to set the context of the transaction
   * 
   * @param beanStoreFactory1 BeanStoreFactory Provides access to the local persistence transaction implementation.
   * @param beanType1 Class<? extends StorableBean> The BeanStore's Bean type this call concerns.
   * @param beanStoreMethod1 String The name of the BeanStore method this call concerns.
   */
  protected BeanStoreTransactionWrapper(final BeanStoreFactory beanStoreFactory1, final Class<? extends StorableBean> beanType1, final String beanStoreMethod1) {
    this.beanStoreFactory = beanStoreFactory1;
    this.beanType = beanType1;
    this.beanStoreMethod = beanStoreMethod1;
  }

  /**
   * Define this method with the meat of your data storage access/modification code.
   * 
   * @param transaction BeanStoreTransaction
   * @return U
   * @throws BeanStoreException
   */
  protected abstract U perform(BeanStoreTransaction transaction) throws BeanStoreException;

  /**
   * Call this to actually carry out the actions of this wrapper.
   * 
   * @return U
   * @throws BeanStoreException
   */
  public final U handle() throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), getBeanStoreMethod());
    U ret = null;
    BeanStoreTransaction transaction = null;
    try {
      transaction = getBeanStoreFactory().beginTransaction(getBeanType(), IMPLICIT_TRANSACTION_NAME);
      ret = perform(transaction);
      getBeanStoreFactory().endTransaction(getBeanType(), transaction);
      return ret;
    } catch(Exception e) {
      throw new BeanStoreException(new BeanStoreStackInfo(getBeanType(), transaction, getBeanStoreMethod()), e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), getBeanStoreMethod(), ret);
    }
  }

  /**
   * Uses the defined BeanPropertyValueAdapter to convert a String into a particular bean property's type,
   * for use in matching.
   *
   * @param transaction BeanStoreTransaction
   * @param propertyName String
   * @param propertyValue String
   * @return Object
   */
  protected Object castAsPropertyTypeValue(BeanStoreTransaction transaction, String propertyName, String propertyValue) {
    BeanPropertyValueAdapter bpva = getBeanStoreFactory().getBeanPropertyValueAdapter();
    return bpva.getAdaptedValue(new BeanStoreStackInfo(getBeanType(), transaction, getBeanStoreMethod()), propertyName, propertyValue);
  }
}
