package org.tharrisx.framework.store.memory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.BeanList;
import org.tharrisx.framework.bean.PageableBeanList;
import org.tharrisx.framework.store.BeanStoreFactory;
import org.tharrisx.framework.store.BeanStoreTransaction;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.framework.store.core.AbstractBeanStore;
import org.tharrisx.framework.store.core.BeanStoreStackInfo;
import org.tharrisx.framework.store.core.BeanStoreTransactionWrapper;
import org.tharrisx.framework.store.exception.BeanStoreException;
import org.tharrisx.framework.store.exception.ConflictingBeansFoundException;
import org.tharrisx.framework.store.exception.NoBeanFoundException;
import org.tharrisx.framework.store.exception.OperationNotSupportedException;
import org.tharrisx.util.log.Log;

/**
 * Simple test implementation.
 * 
 * DO NOT USE IN PRODUCTION... this class uses reflection to match bean property values against the actual beans,
 * and due to that, this impl. will be fairly slow. Also, storing all beans in memory? It's a store... use some
 * real storage, like a man.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class MemoryBeanStore<T extends StorableBean> extends AbstractBeanStore<T> {

  private abstract class MemoryBeanStoreTransactionWrapper<U> extends BeanStoreTransactionWrapper<U> {

    MemoryBeanStoreTransactionWrapper(final BeanStoreFactory beanStoreFactory1, final Class<? extends StorableBean> beanType1, final String beanStoreMethod1) {
      super(beanStoreFactory1, beanType1, beanStoreMethod1);
    }

    /**
     * The memory impl. of BeanStore uses reflection to access the bean property values for matching. This
     * guarantees that the Memory impl. won't be used in production, as it will be fairly slow.
     * 
     * @param transaction BeanStoreTransaction
     * @param beanStoreMethod String
     * @param bean T
     * @param matchingPropertyEntry Map.Entry<String, String>
     * @return boolean
     */
    boolean propertyMatchesValue(BeanStoreTransaction transaction, String beanStoreMethod, T bean, Map.Entry<String, String> matchingPropertyEntry) {
      boolean ret = false;
      BeanStoreStackInfo stackInfo = new BeanStoreStackInfo(getBeanType(), transaction, beanStoreMethod);
      try {
        Object potentialBeanPropertyValue = getBeanType().getField(matchingPropertyEntry.getKey()).get(bean);
        Object beanEquivValue = getBeanStoreFactory().getBeanPropertyValueAdapter().getAdaptedValue(stackInfo, matchingPropertyEntry.getKey(), matchingPropertyEntry.getValue());
        ret = (beanEquivValue.equals(potentialBeanPropertyValue));
        return ret;
      } catch(IllegalAccessException e) {
        Log.error(getClass(), "propertyMatchesValue", e);
        throw new BeanStoreException(stackInfo, e);
      } catch(NoSuchFieldException e) {
        Log.error(getClass(), "propertyMatchesValue", e);
        throw new BeanStoreException(stackInfo, e);
      }
    }
  }

  // properties

  private final Map<String, T> cache = new HashMap<>();

  Map<String, T> getCache() {
    return this.cache;
  }

  // constructor

  MemoryBeanStore(final MemoryBeanStoreFactory beanStoreFactory1, final Class<T> beanType1) {
    super(beanStoreFactory1, beanType1);
  }

  // BeanStore implementation

  @Override
  public boolean hasBean(final String beanId) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_HAS_BEAN, beanId);
    boolean ret = false;
    try {
      ret = new MemoryBeanStoreTransactionWrapper<Boolean>(getBeanStoreFactory(), getBeanType(), METHOD_HAS_BEAN) {
        @Override
        protected Boolean perform(BeanStoreTransaction transaction) throws BeanStoreException {
          return getCache().containsKey(beanId);
        }
      }.handle().booleanValue();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_HAS_BEAN, ret);
    }
  }

  @Override
  public boolean hasBeanByUniqueKey(final Map<String, String> propertyValues) throws ConflictingBeansFoundException, BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_HAS_BEAN_BY_UNIQUE_KEY, propertyValues);
    boolean ret = false;
    try {
      ret = new MemoryBeanStoreTransactionWrapper<Boolean>(getBeanStoreFactory(), getBeanType(), METHOD_HAS_BEAN) {
        @Override
        protected Boolean perform(BeanStoreTransaction transaction) throws BeanStoreException {
          Boolean retInner = Boolean.FALSE;
          int matches = 0;
          outer: for(T bean : getCache().values()) {
            for(Map.Entry<String, String> propertyValue : propertyValues.entrySet()) {
              if(!propertyMatchesValue(transaction, METHOD_HAS_BEAN_BY_UNIQUE_KEY, bean, propertyValue))
                continue outer;
            }
            matches++;
          }
          if(matches == 1) {
            retInner = true;
          } else if(matches > 1) {
            throw new ConflictingBeansFoundException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_HAS_BEAN_BY_UNIQUE_KEY), matches, propertyValues);
          }
          return retInner;
        }
      }.handle().booleanValue();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_HAS_BEAN_BY_UNIQUE_KEY, ret);
    }
  }

  @Override
  public T getBean(final String beanId) throws NoBeanFoundException, BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_BEAN, beanId);
    T ret = null;
    try {
      ret = new MemoryBeanStoreTransactionWrapper<T>(getBeanStoreFactory(), getBeanType(), METHOD_GET_BEAN) {
        @Override
        protected T perform(BeanStoreTransaction transaction) throws BeanStoreException {
          T retInner = null;
          retInner = getCache().get(beanId);
          if(null == retInner) {
            throw new NoBeanFoundException(new BeanStoreStackInfo(getBeanType(), transaction, "getBean"), beanId);
          }
          return retInner;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_BEAN, ret);
    }
  }

  @Override
  public T getBeanByUniqueKey(final Map<String, String> propertyValues) throws NoBeanFoundException, ConflictingBeansFoundException, BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_BEAN_BY_UNIQUE_KEY, propertyValues);
    T ret = null;
    try {
      ret = new MemoryBeanStoreTransactionWrapper<T>(getBeanStoreFactory(), getBeanType(), METHOD_GET_BEAN_BY_UNIQUE_KEY) {
        @Override
        protected T perform(BeanStoreTransaction transaction) throws BeanStoreException {
          T retInner = null;
          int matches = 0;
          outer: for(T bean : getCache().values()) {
            for(Map.Entry<String, String> propertyValue : propertyValues.entrySet()) {
              if(!propertyMatchesValue(transaction, METHOD_GET_BEAN_BY_UNIQUE_KEY, bean, propertyValue))
                continue outer;
            }
            if(null != bean)
              retInner = bean;
            matches++;
          }
          if(matches > 1) {
            throw new ConflictingBeansFoundException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_GET_BEAN_BY_UNIQUE_KEY), matches, propertyValues);
          }
          return retInner;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_GET_BEAN_BY_UNIQUE_KEY, ret);
    }
  }

  @Override
  public BeanList<T> getAllMatchingBeans(final Map<String, String> propertyValues) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_ALL_MATCHING_BEANS, propertyValues);
    BeanList<T> ret = null;
    try {
      ret = new MemoryBeanStoreTransactionWrapper<BeanList<T>>(getBeanStoreFactory(), getBeanType(), METHOD_GET_ALL_MATCHING_BEANS) {
        @Override
        protected BeanList<T> perform(BeanStoreTransaction transaction) throws BeanStoreException {
          BeanList<T> retInner = null;
          List<T> list = new LinkedList<>();
          outer: for(T bean : getCache().values()) {
            for(Map.Entry<String, String> propertyValue : propertyValues.entrySet()) {
              if(!propertyMatchesValue(transaction, METHOD_GET_ALL_MATCHING_BEANS, bean, propertyValue))
                continue outer;
            }
            list.add(bean);
          }
          retInner = new BeanList<>(list);
          return retInner;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_ALL_MATCHING_BEANS, ret);
    }
  }

  @Override
  public BeanList<T> getAllBeansViaQuery(String queryName, Object... queryParameters) throws BeanStoreException {
    throw new OperationNotSupportedException(new BeanStoreStackInfo(getBeanType(), null, METHOD_GET_ALL_BEANS_VIA_QUERY), "Named queries are not supported by the MemoryBeanStore.");
  }

  @Override
  public PageableBeanList<T> getPageOfMatchingBeans(final int start, final int end, final Map<String, String> propertyValues) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_PAGE_OF_MATCHING_BEANS, propertyValues);
    PageableBeanList<T> ret = null;
    try {
      ret = new MemoryBeanStoreTransactionWrapper<PageableBeanList<T>>(getBeanStoreFactory(), getBeanType(), METHOD_GET_PAGE_OF_MATCHING_BEANS) {
        @Override
        protected PageableBeanList<T> perform(BeanStoreTransaction transaction) throws BeanStoreException {
          PageableBeanList<T> retInner = null;
          List<T> list = new LinkedList<>();
          int matches = 0;
          outer: for(T bean : getCache().values()) {
            for(Map.Entry<String, String> propertyValue : propertyValues.entrySet()) {
              if(!propertyMatchesValue(transaction, "getPageOfBeans", bean, propertyValue))
                continue outer;
            }
            list.add(bean);
            matches++;
          }
          if(Log.isDebugEnabled(getClass())) Log.debug(getClass(), METHOD_GET_PAGE_OF_MATCHING_BEANS, "list: " + list + ", matches: " + matches);
          List<T> page = null;
          if(list.size() < start) {
            page = new LinkedList<>(); // start is past the end of the list, return an empty page
          } else {
            int localEnd = end;
            if(localEnd > list.size()) {
              localEnd = list.size(); // end is past the end of the list, return a short page
            }
            page = list.subList(start, localEnd);
          }
          if(Log.isDebugEnabled(getClass())) Log.debug(getClass(), METHOD_GET_PAGE_OF_MATCHING_BEANS, "page: " + page + ", matches: " + matches);
          retInner = new PageableBeanList<>(page, matches, start, end);
          return retInner;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_PAGE_OF_MATCHING_BEANS, ret);
    }
  }

  @Override
  public PageableBeanList<T> getPageOfBeansViaQuery(int start, int end, String queryName, Object... queryParameters) throws BeanStoreException {
    throw new OperationNotSupportedException(new BeanStoreStackInfo(getBeanType(), null, METHOD_GET_PAGE_OF_BEANS_VIA_QUERY), "Named queries are not supported by the MemoryBeanStore.");
  }

  @Override
  public T createOrUpdateBean(T bean, BeanStoreTransaction transaction) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_CREATE_OR_UPDATE_BEAN, bean, transaction);
    T ret = bean;
    try {
      if(wasBeanStoredPreviously(bean)) {
        // create
        bean.setId(createBeanId());
        handleStampedBeanCreate(bean);
        getCache().put(bean.getId(), bean);
      } else {
        // update
        handleStampedBeanUpdate(bean);
        getCache().remove(bean.getId());
        getCache().put(bean.getId(), bean);
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_CREATE_OR_UPDATE_BEAN);
    }
  }

  @Override
  public boolean deleteBean(String beanId, BeanStoreTransaction transaction) throws NoBeanFoundException, BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_DELETE_BEAN, beanId, transaction);
    boolean ret = false;
    try {
      T bean = getCache().remove(beanId);
      ret = null != bean;
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_DELETE_BEAN, ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .appendSuper(super.toString())
        .append("cache", getCache())
        .toString();
  }
}
