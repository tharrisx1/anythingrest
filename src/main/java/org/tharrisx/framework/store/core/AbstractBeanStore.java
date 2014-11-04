package org.tharrisx.framework.store.core;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.StampedBean;
import org.tharrisx.framework.store.BeanStore;
import org.tharrisx.framework.store.BeanStoreFactory;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.RandomGUID;
import org.tharrisx.util.log.Log;

/**
 * Some helpful, non-persistence layer specific, method implementations defining behavior
 * all BeanStores should share, especially around the create and update operations.
 * 
 * @param <T> T extends StorableBean
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class AbstractBeanStore<T extends StorableBean> implements BeanStore<T> {

  private final BeanStoreFactory beanStoreFactory;

  public BeanStoreFactory getBeanStoreFactory() {
    return this.beanStoreFactory;
  }

  private final Class<T> beanType;

  public Class<T> getBeanType() {
    return this.beanType;
  }

  protected AbstractBeanStore(final BeanStoreFactory beanStoreFactory1, final Class<T> beanType1) {
    if(Log.isEnteringEnabled(AbstractBeanStore.class))
      Log.entering(AbstractBeanStore.class, Log.METHOD_NAME_CONSTRUCTOR, beanStoreFactory1, beanType1);
    this.beanStoreFactory = beanStoreFactory1;
    this.beanType = beanType1;
    if(Log.isExitingEnabled(AbstractBeanStore.class))
      Log.exiting(AbstractBeanStore.class, Log.METHOD_NAME_CONSTRUCTOR);
  }

  /**
   * Is this bean transient or persisted? In other words, does the bean id have a value?
   * 
   * @param bean T
   * @return boolean
   */
  protected boolean wasBeanStoredPreviously(T bean) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "wasBeanStoredPreviously", bean);
    boolean ret = false;
    try {
      ret = null != bean.getId() && !"".equals(bean.getId());
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "wasBeanStoredPreviously", ret);
    }
  }

  /**
   * Create a 32 character GUID String id for the beans. ($$$ Add auto-generation via persistence.)
   * @return String
   */
  protected String createBeanId() {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "createBeanId");
    String ret = null;
    try {
      ret = new RandomGUID(false).toString();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "createBeanId", ret);
    }
  }

  /**
   * Fill in the created and lastChanged stamps on create operations if the Bean supports it.
   * 
   * @param bean T
   */
  protected void handleStampedBeanCreate(T bean) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "handleStampedBeanCreate", bean);
    try {
      if(bean instanceof StampedBean) {
        ((StampedBean) bean).setCreated(new Date());
        ((StampedBean) bean).setLastChanged(new Date());
      }
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "handleStampedBeanCreate");
    }
  }

  /**
   * Fill in the lastChanged stamps on update operations if the Bean supports it.
   * 
   * @param bean T
   */
  protected void handleStampedBeanUpdate(T bean) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "handleStampedBeanUpdate", bean);
    try {
      if(bean instanceof StampedBean) {
        ((StampedBean) bean).setLastChanged(new Date());
      }
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "handleStampedBeanUpdate");
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        //.append("beanStoreFactory", getBeanStoreFactory())
        .append("beanType", getBeanType()).toString();
  }
}
