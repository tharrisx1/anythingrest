package org.tharrisx.framework.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.BeanList;
import org.tharrisx.framework.bean.PageableBeanList;
import org.tharrisx.framework.rest.core.ServicesRegistry;
import org.tharrisx.framework.store.BeanStore;
import org.tharrisx.framework.store.BeanStoreTransaction;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;

/**
 * Defines the implementation of the generic API for a specific bean type. This can be used as the
 * default bean behavior by omitting the Behavior annotation from a StorableBean, and then
 * referring to it in the REST framework, either via a RootedTypeResourceName annotation on the bean,
 * or as a child resource returned from Path annotated method in another resource.
 * 
 * Define your own subclasses of this to add more specific, perhaps query based, methods for your
 * bean type, to traverse child collections, for example.
 * 
 * This class links up the use of the BeanStore interface to the rest package.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class BeanBehavior<T extends StorableBean> {

  private final BeanStore<T> beanStore;

  public final BeanStore<T> getBeanStore() {
    return this.beanStore;
  }

  private final Class<T> beanType;

  public final Class<T> getBeanType() {
    return this.beanType;
  }

  public BeanBehavior(final Class<T> beanType1) {
    if(Log.isEnteringEnabled(BeanBehavior.class)) Log.entering(BeanBehavior.class, Log.METHOD_NAME_CONSTRUCTOR, beanType1);
    try {
      this.beanType = beanType1;
      this.beanStore = ServicesRegistry.getServices().getBeanStoreFactory().getBeanStore(getBeanType());
    } finally {
      if(Log.isExitingEnabled(BeanBehavior.class)) Log.exiting(BeanBehavior.class, Log.METHOD_NAME_CONSTRUCTOR);
    }
  }

  public BeanList<T> getAllBeans() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "getAllBeans");
    BeanList<T> ret = null;
    try {
      Map<String, String> propertyValues = new HashMap<>();
      ret = getBeanStore().getAllMatchingBeans(propertyValues);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "getAllBeans", ret);
    }
  }

  public PageableBeanList<T> getBeans(int start, int end) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "getBeans", start, end);
    PageableBeanList<T> ret = null;
    try {
      Map<String, String> propertyValues = new HashMap<>();
      ret = matchBeans(start, end, propertyValues);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "getBeans", ret);
    }
  }

  public PageableBeanList<T> matchBeans(int start, int end, Map<String, String> propertyValues) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "matchBeans", start, end, propertyValues);
    PageableBeanList<T> ret = null;
    try {
      ret = getBeanStore().getPageOfMatchingBeans(start, end, propertyValues);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "matchBeans", ret);
    }
  }

  public T getBean(String beanId) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "getBean", beanId);
    T ret = null;
    try {
      ret = getBeanStore().getBean(beanId);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "getBean", ret);
    }
  }

  public T postBean(T bean) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "postBean", bean);
    T ret = null;
    try {
      BeanStoreTransaction transaction = ServicesRegistry.getServices().getBeanStoreFactory().beginTransaction(getBeanType(), "postBean");
      ret = getBeanStore().createOrUpdateBean(bean, transaction);
      ServicesRegistry.getServices().getBeanStoreFactory().endTransaction(getBeanType(), transaction);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "postBean", ret);
    }
  }

  public T putBean(T bean) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "putBean", bean);
    T ret = null;
    try {
      BeanStoreTransaction transaction = ServicesRegistry.getServices().getBeanStoreFactory().beginTransaction(getBeanType(), "putBean");
      ret = getBeanStore().createOrUpdateBean(bean, transaction);
      ServicesRegistry.getServices().getBeanStoreFactory().endTransaction(getBeanType(), transaction);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "putBean", ret);
    }
  }

  public boolean deleteBean(String beanId) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "deleteBean", beanId);
    boolean ret = false;
    try {
      BeanStoreTransaction transaction = ServicesRegistry.getServices().getBeanStoreFactory().beginTransaction(getBeanType(), "deleteBean");
      ret = getBeanStore().deleteBean(beanId, transaction);
      ServicesRegistry.getServices().getBeanStoreFactory().endTransaction(getBeanType(), transaction);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "deleteBean", ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("beanStore", getBeanStore()).append("beanType", getBeanType()).toString();
  }
}
