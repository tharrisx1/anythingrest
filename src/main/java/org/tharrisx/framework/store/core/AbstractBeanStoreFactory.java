package org.tharrisx.framework.store.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.store.BeanPropertyValueAdapter;
import org.tharrisx.framework.store.BeanStore;
import org.tharrisx.framework.store.BeanStoreFactory;
import org.tharrisx.framework.store.DefaultBeanPropertyValueAdapter;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.framework.store.exception.OperationNotSupportedException;
import org.tharrisx.util.log.Log;

/**
 * Implements some methods for implementers of BeanStore so they don't have to.
 * 
 * - Maintains the BeanStore cache; just define your constructBeanStore implementation.
 * - Uses the DefaultBeanPropertyValueAdapter as the BeanPropertyValueAdapter impl.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class AbstractBeanStoreFactory implements BeanStoreFactory {

  protected final static String METHOD_GET_BEAN_PROPERTY_VALUE_ADAPTER = "getBeanPropertyValueAdapter";
  protected final static String METHOD_GET_BEAN_STORE = "getBeanStore";
  protected final static String METHOD_CONSTRUCT_BEAN_STORE = "constructBeanStore";
  protected final static String METHOD_BEGIN_TRANSACTION = "beginTransaction";
  protected final static String METHOD_END_TRANSACTION = "endTransaction";

  private final List<Class<? extends StorableBean>> beanTypes;

  private List<Class<? extends StorableBean>> getBeanTypes() {
    return this.beanTypes;
  }

  private final Map<Class<? extends StorableBean>, BeanStore<? extends StorableBean>> beanStoreCache = new HashMap<>();

  private Map<Class<? extends StorableBean>, BeanStore<? extends StorableBean>> getBeanStoreCache() {
    return this.beanStoreCache;
  }

  protected AbstractBeanStoreFactory(List<Class<? extends StorableBean>> beanTypes1) {
    this.beanTypes = beanTypes1;
  }

  /**
   * Implementers must define this method to create Bean type-specific BeanStores.
   * 
   * @param <T> T extends StorableBean
   * @param type Class<T>
   * @return BeanStore<T>
   */
  protected abstract <T extends StorableBean> BeanStore<T> constructBeanStore(Class<T> type);

  @Override
  public BeanPropertyValueAdapter getBeanPropertyValueAdapter() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_BEAN_PROPERTY_VALUE_ADAPTER);
    BeanPropertyValueAdapter ret = null;
    try {
      ret = new DefaultBeanPropertyValueAdapter();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_BEAN_PROPERTY_VALUE_ADAPTER, ret);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T extends StorableBean> BeanStore<T> getBeanStore(Class<T> type) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_BEAN_STORE, type);
    BeanStore<T> ret = null;
    try {
      if(!getBeanTypes().contains(type)) {
        throw new OperationNotSupportedException(new BeanStoreStackInfo(type, null, METHOD_GET_BEAN_STORE), "The requested bean type '" + type.getName()
            + "' is not configured, and so is not permitted for use.");
      }
      BeanStore<? extends Bean> potentialBeanStore = getBeanStoreCache().get(type);
      if(null != potentialBeanStore) {
        ret = (BeanStore<T>) potentialBeanStore;
      } else {
        ret = constructBeanStore(type);
        getBeanStoreCache().put(type, ret);
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_BEAN_STORE, ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("beanTypes", getBeanTypes())
        //.append("beanStoreCache", getBeanStoreCache())
        .toString();
  }
}
