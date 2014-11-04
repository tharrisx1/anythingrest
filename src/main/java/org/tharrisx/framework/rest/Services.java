package org.tharrisx.framework.rest;

import java.util.List;

import org.tharrisx.framework.callstats.CallStatistician;
import org.tharrisx.framework.pipe.PipeManager;
import org.tharrisx.framework.store.BeanStoreFactory;
import org.tharrisx.framework.store.StorableBean;

/**
 * The application must define an implementation of this interface to provide the rest resources the hooks they
 * need to perform their magic.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface Services {

  /**
   * Provide the list of your application's supported StorableBean classes
   * 
   * @return List<Class<? extends StorableBean>>
   */
  List<Class<? extends StorableBean>> getStorableBeanTypes();

  /**
   * Provide your application's bean store factory implementation.
   * 
   * @return BeanStoreFactory
   */
  BeanStoreFactory getBeanStoreFactory();

  /**
   * Provide your pre-configured PipeManager.
   * 
   * @return PipeManager
   */
  PipeManager getPipeManager();

  CallStatistician getCallStatistician();
}
