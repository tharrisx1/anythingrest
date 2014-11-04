package org.tharrisx.framework.rest.core;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.ws.rs.WebApplicationException;

import org.tharrisx.framework.rest.Services;
import org.tharrisx.util.log.Log;

/**
 * Application scope objects the resources will require access to.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ServicesRegistry {

  private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private static final Lock read = readWriteLock.readLock();
  private static final Lock write = readWriteLock.writeLock();

  private static Services services = null;
  private static BeanConfiguration beanConfiguration = null;

  public static Services getServices() {
    if(Log.isEnteringEnabled(Services.class))
      Log.entering(Services.class, "getServices");
    Services ret = null;
    try {
      read.lock();
      if(null == services)
        throw new WebApplicationException();
      ret = services;
      return ret;
    } finally {
      read.unlock();
      if(Log.isExitingEnabled(Services.class))
        Log.exiting(Services.class, "getServices", ret);
    }
  }

  public static BeanConfiguration getBeanConfiguration() {
    if(Log.isEnteringEnabled(Services.class))
      Log.entering(Services.class, "getBeanConfiguration");
    BeanConfiguration ret = null;
    try {
      read.lock();
      if(null == beanConfiguration)
        throw new WebApplicationException();
      ret = beanConfiguration;
      return ret;
    } finally {
      read.unlock();
      if(Log.isExitingEnabled(Services.class))
        Log.exiting(Services.class, "getBeanConfiguration", ret);
    }
  }

  public static void registerServices(final Services arg) {
    if(Log.isEnteringEnabled(Services.class))
      Log.entering(Services.class, "registerServices");
    try {
      write.lock();
      services = arg;
      beanConfiguration = new BeanConfiguration(services);
    } finally {
      write.unlock();
      if(Log.isExitingEnabled(Services.class))
        Log.exiting(Services.class, "registerServices");
    }
  }
}
