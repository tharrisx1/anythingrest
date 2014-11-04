package org.tharrisx.framework.rest;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.tharrisx.framework.rest.core.BaseServices;
import org.tharrisx.framework.rest.core.ServicesRegistry;
import org.tharrisx.framework.store.BeanStoreFactory;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.framework.store.hibernate.HibernateBeanStoreFactory;
import org.tharrisx.util.log.Log;

/**
 * Spring-configurable Services bean, with a Hibernate-based Bean Store.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Configurable
public class HibernateServices extends BaseServices {

  private final BeanStoreFactory beanStoreFactory;

  @Override
  public BeanStoreFactory getBeanStoreFactory() {
    return this.beanStoreFactory;
  }

  public HibernateServices(final List<Class<? extends StorableBean>> storableBeanTypes1, final SessionFactory sessionFactory1, final boolean enableCallStatistics1) {
    super(storableBeanTypes1, enableCallStatistics1);
    if(Log.isEnteringEnabled(HibernateServices.class))
      Log.entering(HibernateServices.class, Log.METHOD_NAME_CONSTRUCTOR);
    try {
      this.beanStoreFactory = new HibernateBeanStoreFactory(getStorableBeanTypes(), sessionFactory1);
      ServicesRegistry.registerServices(this);
    } finally {
      if(Log.isExitingEnabled(HibernateServices.class))
        Log.exiting(HibernateServices.class, Log.METHOD_NAME_CONSTRUCTOR);
    }
  }
}
