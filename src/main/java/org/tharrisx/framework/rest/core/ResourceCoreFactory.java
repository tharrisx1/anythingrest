package org.tharrisx.framework.rest.core;

import java.util.HashMap;
import java.util.Map;

import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;

/**
 * Caches lazily instantiated ResourceCores for each bean type.
 * 
 * @author tharrisx
 * @since 3.0.0
 * @version 3.0.0
 */
public class ResourceCoreFactory {

  private static final String METHOD_GET_RESOURCE_CORE = "getResourceCore";

  private static Map<Class<? extends StorableBean>, ResourceCore<? extends StorableBean>> CORES = new HashMap<>();

  @SuppressWarnings("unchecked") // trickery
  public static <T extends StorableBean> ResourceCore<T> getResourceCore(Class<T> beanType) {
    if(Log.isEnteringEnabled(ResourceCoreFactory.class)) Log.entering(ResourceCoreFactory.class, METHOD_GET_RESOURCE_CORE, beanType);
    ResourceCore<T> ret = null;
    try {
      if(Log.isTraceEnabled(ResourceCoreFactory.class)) Log.trace(ResourceCoreFactory.class, METHOD_GET_RESOURCE_CORE, "Waiting for CORES mutex...");
      synchronized(CORES) {
        if(Log.isTraceEnabled(ResourceCoreFactory.class)) Log.trace(ResourceCoreFactory.class, METHOD_GET_RESOURCE_CORE, "Got CORES mutex.");
        ret = (ResourceCore<T>) CORES.get(beanType);
        if(null == ret) {
          if(Log.isTraceEnabled(ResourceCoreFactory.class)) Log.trace(ResourceCoreFactory.class, METHOD_GET_RESOURCE_CORE, "CORES cache miss, creating new ResourceCore for beanType: " + beanType);
          ret = new ResourceCore<>(beanType);
          CORES.put(beanType, ret);
        } else {
          if(Log.isTraceEnabled(ResourceCoreFactory.class)) Log.trace(ResourceCoreFactory.class, METHOD_GET_RESOURCE_CORE, "CORES cache hit, returning ResourceCore for beanType: " + beanType);
        }
        if(Log.isTraceEnabled(ResourceCoreFactory.class)) Log.trace(ResourceCoreFactory.class, METHOD_GET_RESOURCE_CORE, "Giving up CORES mutex.");
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(ResourceCoreFactory.class)) Log.exiting(ResourceCoreFactory.class, METHOD_GET_RESOURCE_CORE, ret);
    }
  }
}
