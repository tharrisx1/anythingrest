package org.tharrisx.framework.store.memory;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.store.BeanStore;
import org.tharrisx.framework.store.BeanStoreTransaction;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.framework.store.core.AbstractBeanStoreFactory;
import org.tharrisx.framework.store.exception.BeanStoreException;
import org.tharrisx.util.log.Log;

/**
 * Simple test implementation.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class MemoryBeanStoreFactory extends AbstractBeanStoreFactory {

  public MemoryBeanStoreFactory(List<Class<? extends StorableBean>> beanTypes1) {
    super(beanTypes1);
  }

  @Override
  protected <T extends StorableBean> BeanStore<T> constructBeanStore(Class<T> type) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_CONSTRUCT_BEAN_STORE, type);
    BeanStore<T> ret = null;
    try {
      ret = new MemoryBeanStore<>(this, type);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_CONSTRUCT_BEAN_STORE, ret);
    }
  }

  @Override
  public <T extends StorableBean> BeanStoreTransaction beginTransaction(Class<T> mainBeanType, String transactionName) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_BEGIN_TRANSACTION, mainBeanType);
    BeanStoreTransaction ret = null;
    try {
      // Hypothetical transaction begins
      ret = new MemoryBeanStoreTransaction(transactionName);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_BEGIN_TRANSACTION, ret);
    }
  }

  @Override
  public <T extends StorableBean> void endTransaction(Class<T> mainBeanType, BeanStoreTransaction transaction) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_END_TRANSACTION, mainBeanType, transaction);
    try {
      // Hypothetical transaction ends
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_END_TRANSACTION);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).appendSuper(super.toString()).toString();
  }
}
