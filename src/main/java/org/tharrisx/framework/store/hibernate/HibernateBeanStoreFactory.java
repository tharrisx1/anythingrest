package org.tharrisx.framework.store.hibernate;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.tharrisx.framework.store.BeanStore;
import org.tharrisx.framework.store.BeanStoreTransaction;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.framework.store.core.AbstractBeanStoreFactory;
import org.tharrisx.framework.store.core.BeanStoreStackInfo;
import org.tharrisx.framework.store.exception.BeanStoreException;
import org.tharrisx.util.log.Log;

/**
 * Implements BeanStoreFactory using Hibernate.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class HibernateBeanStoreFactory extends AbstractBeanStoreFactory {

  private final SessionFactory sessionFactory;

  private SessionFactory getSessionFactory() {
    return this.sessionFactory;
  }

  public HibernateBeanStoreFactory(List<Class<? extends StorableBean>> beanTypes1, SessionFactory sessionFactory1) {
    super(beanTypes1);
    if(Log.isEnteringEnabled(HibernateBeanStoreFactory.class)) Log.entering(HibernateBeanStoreFactory.class, Log.METHOD_NAME_CONSTRUCTOR, beanTypes1, sessionFactory1);
    try {
      this.sessionFactory = sessionFactory1;
    } finally {
      if(Log.isExitingEnabled(HibernateBeanStoreFactory.class)) Log.exiting(HibernateBeanStoreFactory.class, Log.METHOD_NAME_CONSTRUCTOR);
    }
  }

  @Override
  protected <T extends StorableBean> BeanStore<T> constructBeanStore(Class<T> beanType) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_CONSTRUCT_BEAN_STORE, beanType);
    BeanStore<T> ret = null;
    try {
      ret = new HibernateBeanStore<>(this, beanType);
      if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_CONSTRUCT_BEAN_STORE, "HibernateBeanStore created for bean type " + beanType + ".");
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_CONSTRUCT_BEAN_STORE, ret);
    }
  }

  @Override
  public <T extends StorableBean> BeanStoreTransaction beginTransaction(Class<T> mainBeanType, String transactionName) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_BEGIN_TRANSACTION, mainBeanType, transactionName);
    BeanStoreTransaction ret = null;
    try {
      Session hibSession = getSessionFactory().openSession();
      Transaction hibTransaction = hibSession.beginTransaction();
      ret = new HibernateBeanStoreTransaction(transactionName, hibSession, hibTransaction);
      if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_BEGIN_TRANSACTION, "Transaction begun for mainBeanType " + mainBeanType + " named '" + transactionName + "'.");
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_BEGIN_TRANSACTION, ret);
    }
  }

  @Override
  public <T extends StorableBean> void endTransaction(Class<T> mainBeanType, BeanStoreTransaction transaction) throws BeanStoreException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_END_TRANSACTION, mainBeanType, transaction);
    try {
      if(null == transaction) {
        throw new BeanStoreException(new BeanStoreStackInfo(mainBeanType, null, null), "Transaction was not started.");
      }
      Session hibSession = ((HibernateBeanStoreTransaction) transaction).getSession();
      Transaction hibTransaction = ((HibernateBeanStoreTransaction) transaction).getTransaction();
      if(null == hibSession || null == hibTransaction) {
        throw new BeanStoreException(new BeanStoreStackInfo(mainBeanType, transaction, null), "Hibernate transaction was not started.");
      }
      try {
        // $$$ hibSession.flush();
        hibTransaction.commit();
        if(Log.isInfoEnabled(getClass())) Log.info(getClass(), METHOD_END_TRANSACTION, "Transaction committed for mainBeanType " + mainBeanType + " named '" + transaction.getTransactionName() + "'.");
      } catch(HibernateException e) {
        try {
          Log.warn(getClass(), METHOD_END_TRANSACTION, "Transaction rolling back for mainBeanType " + mainBeanType + " named '" + transaction.getTransactionName() + "'.");
          hibTransaction.rollback();
        } catch(HibernateException e1) {
          Log.fatal(getClass(), METHOD_END_TRANSACTION, "Rollback of transaction failed.", e1);
        }
        throw new BeanStoreException(new BeanStoreStackInfo(mainBeanType, transaction, null), e);
      } finally {
        try {
          hibSession.close();
        } catch(HibernateException e) {
          Log.fatal(getClass(), METHOD_END_TRANSACTION, "Close of session failed.", e);
        }
      }
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_END_TRANSACTION);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).appendSuper(super.toString()).append("sessionFactory", getSessionFactory()).toString();
  }
}
