package org.tharrisx.framework.rest.core;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.BeanList;
import org.tharrisx.framework.bean.PageableBeanList;
import org.tharrisx.framework.bean.ResourceNameBean;
import org.tharrisx.framework.callstats.CallStatistician;
import org.tharrisx.framework.pipe.DefaultPipeContextProtectionFactory;
import org.tharrisx.framework.pipe.PipeContextProtectionFactory;
import org.tharrisx.framework.pipe.PipeManager;
import org.tharrisx.framework.pipe.exception.InvalidBeanTypeException;
import org.tharrisx.framework.pipe.exception.NoPipeFoundException;
import org.tharrisx.framework.rest.Services;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;

public abstract class BaseServices implements Services {

  public static final String PIPE_XML = "xml";
  public static final String PIPE_JSON = "json";

  private final List<Class<? extends StorableBean>> storableBeanTypes;

  @Override
  public List<Class<? extends StorableBean>> getStorableBeanTypes() {
    return this.storableBeanTypes;
  }

  private final PipeManager pipeManager;

  @Override
  public PipeManager getPipeManager() {
    return this.pipeManager;
  }

  private final CallStatistician callStatistician;

  @Override
  public CallStatistician getCallStatistician() {
    return this.callStatistician;
  }

  protected BaseServices(final List<Class<? extends StorableBean>> storableBeanTypes1, final boolean enableCallStatistics1) {
    if(Log.isEnteringEnabled(BaseServices.class))
      Log.entering(BaseServices.class, Log.METHOD_NAME_CONSTRUCTOR);
    try {
      this.storableBeanTypes = storableBeanTypes1;
      this.pipeManager = constructFrameworkPipeManager();
      this.callStatistician = new CallStatistician(enableCallStatistics1);
    } finally {
      if(Log.isExitingEnabled(BaseServices.class))
        Log.exiting(BaseServices.class, Log.METHOD_NAME_CONSTRUCTOR);
    }
  }

  private PipeManager constructFrameworkPipeManager() {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "constructFrameworkPipeManager");
    PipeManager ret = null;
    try {
      PipeContextProtectionFactory pipeContextProtectionFactory = new DefaultPipeContextProtectionFactory();
      ret = new PipeManager();
      ret.registerBean(BeanList.class);
      ret.registerBean(PageableBeanList.class);
      ret.registerBean(ResourceNameBean.class);
      for(Class<? extends StorableBean> beanType : getStorableBeanTypes()) {
        try {
          ret.registerBean(beanType);
        } catch(InvalidBeanTypeException e) {
          Log.error(getClass(), "constructFrameworkPipeManager", e.getMessage(), e);
        }
      }
      try {
        ret.definePipe(PIPE_XML, PipeManager.PIPE_FORMAT_XML, pipeContextProtectionFactory);
        ret.definePipe(PIPE_JSON, PipeManager.PIPE_FORMAT_JSON, pipeContextProtectionFactory);
      } catch(NoPipeFoundException e) {
        Log.error(getClass(), "constructFrameworkPipeManager", e.getMessage(), e);
      }
      ret.initialize();
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "constructFrameworkPipeManager", ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("storableBeanTypes", getStorableBeanTypes()).append("beanStoreFactory", getBeanStoreFactory()).append("pipeManager", getPipeManager()).toString();
  }
}
