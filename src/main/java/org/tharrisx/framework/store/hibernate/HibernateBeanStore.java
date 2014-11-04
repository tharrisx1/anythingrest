package org.tharrisx.framework.store.hibernate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
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
import org.tharrisx.util.log.Log;

/**
 * BeanStore a la Hibernate
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public final class HibernateBeanStore<T extends StorableBean> extends AbstractBeanStore<T> {

  private abstract class HibernateBeanStoreTransactionWrapper<U> extends BeanStoreTransactionWrapper<U> {

    HibernateBeanStoreTransactionWrapper(final BeanStoreFactory beanStoreFactory1, final Class<? extends StorableBean> beanType1, final String beanStoreMethod1) {
      super(beanStoreFactory1, beanType1, beanStoreMethod1);
    }

    protected HibernateBeanStoreTransaction getTransactionCast(BeanStoreTransaction transaction) {
      return (HibernateBeanStoreTransaction) transaction;
    }

    Criteria makePropertyValueCriteria(BeanStoreTransaction transaction, Map<String, String> propertyValues) {
      Criteria ret = getTransactionCast(transaction).getSession().createCriteria(getBeanType());
      Object castValue = null;
      for(Map.Entry<String, String> entry : propertyValues.entrySet()) {
        castValue = castAsPropertyTypeValue(transaction, entry.getKey(), entry.getValue());
        ret.add(Restrictions.eq(entry.getKey(), castValue));
      }
      return ret;
    }
  }

  HibernateBeanStore(final BeanStoreFactory beanStoreFactory1, final Class<T> beanType1) {
    super(beanStoreFactory1, beanType1);
  }

  @Override
  public boolean hasBean(final String beanId) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_HAS_BEAN, beanId);
    boolean ret = false;
    try {
      ret = new HibernateBeanStoreTransactionWrapper<Boolean>(getBeanStoreFactory(), getBeanType(), METHOD_HAS_BEAN) {
        @Override
        protected Boolean perform(BeanStoreTransaction transaction) throws BeanStoreException {
          return Boolean.valueOf(null != getTransactionCast(transaction).getSession().get(getBeanType(), beanId));
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
      ret = new HibernateBeanStoreTransactionWrapper<Boolean>(getBeanStoreFactory(), getBeanType(), METHOD_HAS_BEAN) {
        @Override
        protected Boolean perform(BeanStoreTransaction transaction) throws BeanStoreException {
          Boolean retInner = Boolean.FALSE;

          @SuppressWarnings("unchecked")
          List<T> items = Collections.checkedList(makePropertyValueCriteria(transaction, propertyValues).list(), Object.class);

          if(items.size() > 1) {
            throw new ConflictingBeansFoundException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_HAS_BEAN_BY_UNIQUE_KEY), items.size(), propertyValues);
          }
          T itemRef = null;
          if(!items.isEmpty())
            itemRef = items.get(0);
          if(null == itemRef) {
            throw new NoBeanFoundException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_HAS_BEAN_BY_UNIQUE_KEY), "No beans matched unique criteria: " + propertyValues);
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
      ret = new HibernateBeanStoreTransactionWrapper<T>(getBeanStoreFactory(), getBeanType(), METHOD_GET_BEAN) {

        @Override
        protected T perform(BeanStoreTransaction transaction) throws BeanStoreException {
          T retInner = null;

          @SuppressWarnings("unchecked")
          T itemRef = (T) getTransactionCast(transaction).getSession().get(getBeanType(), beanId);

          if(null == itemRef) {
            throw new NoBeanFoundException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_GET_BEAN), "No bean for id: " + beanId);
          }
          retInner = itemRef; // <- bad cast, thanks hibernate.
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
      ret = new HibernateBeanStoreTransactionWrapper<T>(getBeanStoreFactory(), getBeanType(), METHOD_GET_BEAN_BY_UNIQUE_KEY) {
        @SuppressWarnings("unchecked")
        @Override
        protected T perform(BeanStoreTransaction transaction) throws BeanStoreException {
          T retInner = null;
          List<T> items = Collections.checkedList(makePropertyValueCriteria(transaction, propertyValues).list(), Object.class);
          if(items.size() > 1) {
            throw new ConflictingBeansFoundException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_GET_BEAN_BY_UNIQUE_KEY), items.size(), propertyValues);
          }
          T itemRef = null;
          if(!items.isEmpty())
            itemRef = items.get(0);
          if(null == itemRef) {
            throw new NoBeanFoundException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_GET_BEAN_BY_UNIQUE_KEY), "No beans matched unique criteria: " + propertyValues);
          }
          retInner = itemRef;
          return retInner;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_BEAN_BY_UNIQUE_KEY, ret);
    }
  }

  @Override
  public BeanList<T> getAllMatchingBeans(final Map<String, String> propertyValues) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_ALL_MATCHING_BEANS, propertyValues);
    BeanList<T> ret = null;
    try {
      ret = new HibernateBeanStoreTransactionWrapper<BeanList<T>>(getBeanStoreFactory(), getBeanType(), METHOD_GET_ALL_MATCHING_BEANS) {
        @Override
        protected BeanList<T> perform(BeanStoreTransaction transaction) throws BeanStoreException {

          @SuppressWarnings("unchecked")
          BeanList<T> retBeanList = new BeanList<>(Collections.checkedList(makePropertyValueCriteria(transaction, propertyValues).list(), Object.class));

          return retBeanList;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_ALL_MATCHING_BEANS, ret);
    }
  }

  @Override
  public BeanList<T> getAllBeansViaQuery(final String queryName, final Object... queryParameters) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_ALL_BEANS_VIA_QUERY, queryName, queryParameters);
    BeanList<T> ret = null;
    try {
      ret = new HibernateBeanStoreTransactionWrapper<BeanList<T>>(getBeanStoreFactory(), getBeanType(), METHOD_GET_ALL_BEANS_VIA_QUERY) {
        @Override
        protected BeanList<T> perform(BeanStoreTransaction transaction) throws BeanStoreException {

          @SuppressWarnings("unchecked")
          BeanList<T> retBeanList = new BeanList<>(getTransactionCast(transaction).getSession().getNamedQuery(queryName).list());

          return retBeanList;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_ALL_BEANS_VIA_QUERY, ret);
    }
  }

  @Override
  public PageableBeanList<T> getPageOfMatchingBeans(final int start, final int end, final Map<String, String> propertyValues) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_PAGE_OF_MATCHING_BEANS, propertyValues);
    PageableBeanList<T> ret = null;
    try {
      ret = new HibernateBeanStoreTransactionWrapper<PageableBeanList<T>>(getBeanStoreFactory(), getBeanType(), METHOD_GET_PAGE_OF_MATCHING_BEANS) {

        @Override
        protected PageableBeanList<T> perform(BeanStoreTransaction transaction) throws BeanStoreException {
          Criteria countCrit = makePropertyValueCriteria(transaction, propertyValues);
          Criteria pageCrit = makePropertyValueCriteria(transaction, propertyValues).setFirstResult(start).setMaxResults(end - start + 1);

          @SuppressWarnings("unchecked")
          PageableBeanList<T> retBeanList = new PageableBeanList<>(Collections.checkedList(pageCrit.list(), Object.class), countCrit.list().size(), start, end);

          return retBeanList;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_PAGE_OF_MATCHING_BEANS, ret);
    }
  }

  @Override
  public PageableBeanList<T> getPageOfBeansViaQuery(final int start, final int end, final String queryName, final Object... queryParameters) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_PAGE_OF_BEANS_VIA_QUERY, queryName, queryParameters);
    PageableBeanList<T> ret = null;
    try {
      ret = new HibernateBeanStoreTransactionWrapper<PageableBeanList<T>>(getBeanStoreFactory(), getBeanType(), METHOD_GET_PAGE_OF_BEANS_VIA_QUERY) {

        @Override
        protected PageableBeanList<T> perform(BeanStoreTransaction transaction) throws BeanStoreException {
          Query countQuery = getTransactionCast(transaction).getSession().getNamedQuery(queryName);
          Query pageQuery = getTransactionCast(transaction).getSession().getNamedQuery(queryName).setFirstResult(start).setMaxResults(end - start + 1);

          @SuppressWarnings("unchecked")
          PageableBeanList<T> retBeanList = new PageableBeanList<>(pageQuery.list(), countQuery.list().size(), start, end);

          return retBeanList;
        }
      }.handle();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_PAGE_OF_BEANS_VIA_QUERY, ret);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T createOrUpdateBean(T bean, BeanStoreTransaction transaction) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_CREATE_OR_UPDATE_BEAN, bean, transaction);
    T ret = null;
    try {
      HibernateBeanStoreTransaction transactionCast = (HibernateBeanStoreTransaction) transaction;
      if(!wasBeanStoredPreviously(bean)) {
        // create
        handleStampedBeanCreate(bean);
        try {
          bean.setId(createBeanId());
          if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_CREATE_OR_UPDATE_BEAN, "Bean creating for BeanType " + getBeanType()
              + " bean " + bean + " as part of transaction '" + transaction.getTransactionName() + "'.");
          transactionCast.getSession().save(bean);
          ret = (T) transactionCast.getSession().load(getBeanType(), bean.getId());
          if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_CREATE_OR_UPDATE_BEAN, "Bean created for BeanType " + getBeanType()
              + " bean " + ret + " as part of transaction '" + transaction.getTransactionName() + "'.");
        } catch(HibernateException e) {
          throw new BeanStoreException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_CREATE_OR_UPDATE_BEAN), e);
        }
      } else {
        // update
        handleStampedBeanUpdate(bean);
        try {
          if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_CREATE_OR_UPDATE_BEAN, "Bean updating for BeanType " + getBeanType()
              + " bean " + bean + " as part of transaction '" + transaction.getTransactionName() + "'.");
          transactionCast.getSession().update(bean);
          ret = (T) transactionCast.getSession().load(getBeanType(), bean.getId());
          if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_CREATE_OR_UPDATE_BEAN, "Bean updated for BeanType " + getBeanType()
              + " bean " + ret + " as part of transaction '" + transaction.getTransactionName() + "'.");
        } catch(HibernateException e) {
          throw new BeanStoreException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_CREATE_OR_UPDATE_BEAN), e);
        }
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_CREATE_OR_UPDATE_BEAN, ret);
    }
  }

  @Override
  public boolean deleteBean(String beanId, BeanStoreTransaction transaction) throws NoBeanFoundException, BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_DELETE_BEAN, beanId, transaction);
    boolean ret = false;
    try {
      HibernateBeanStoreTransaction transactionCast = (HibernateBeanStoreTransaction) transaction;
      try {
        Object itemRef = transactionCast.getSession().get(getBeanType(), beanId);
        if(null != itemRef) {
          transactionCast.getSession().delete(itemRef);
          if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_DELETE_BEAN, "Bean deleted for BeanType " + getBeanType()
              + " id '" + beanId + "' as part of transaction '" + transaction.getTransactionName() + "'.");
          ret = true;
        }
      } catch(HibernateException e) {
        throw new BeanStoreException(new BeanStoreStackInfo(getBeanType(), transaction, METHOD_DELETE_BEAN), e);
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_DELETE_BEAN, ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).appendSuper(super.toString()).toString();
  }
}
