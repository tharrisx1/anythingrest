package org.tharrisx.framework.pipe.core;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.DefaultPipeContextProtectionFactory;
import org.tharrisx.framework.pipe.Pipe;
import org.tharrisx.framework.pipe.PipeContextProtectionFactory;
import org.tharrisx.framework.pipe.PipeFormat;
import org.tharrisx.framework.pipe.exception.InvalidBeanTypeException;
import org.tharrisx.framework.pipe.exception.NoPipeFoundException;
import org.tharrisx.util.log.Log;
import org.tharrisx.util.text.StringUtils;

import com.thoughtworks.xstream.XStream;

/**
 * Inner core implementation of the PipeManager. Don't use this class externally, use the PipeManager
 * facade instead.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class PipeManagerCore {

  // live configuration
  private final Map<String, Pipe> pipes = new HashMap<>();

  private Map<String, Pipe> getPipes() {
    return this.pipes;
  }

  private final Set<Class<? extends Bean>> registeredBeanTypes = new HashSet<>();

  private Set<Class<? extends Bean>> getRegisteredBeanTypes() {
    return this.registeredBeanTypes;
  }

  public void registerBean(Class<? extends Bean> beanType) throws InvalidBeanTypeException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "registerBean", beanType);
    try {
      beanType.newInstance(); // test now, to make sure, one time cost at start only
      getRegisteredBeanTypes().add(beanType);
    } catch(InstantiationException e) {
      throw new InvalidBeanTypeException(e);
    } catch(IllegalAccessException e) {
      throw new InvalidBeanTypeException(e);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "registerBean");
    }
  }

  public void definePipe(final String pipeName, final Class<? extends PipeFormat> pipeFormat) throws NoPipeFoundException {
    definePipe(pipeName, pipeFormat, new DefaultPipeContextProtectionFactory());
  }

  public void definePipe(final String pipeName, final Class<? extends PipeFormat> pipeFormatType, final PipeContextProtectionFactory pipeContextProtectionFactory) throws NoPipeFoundException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "definePipe", pipeName, pipeFormatType, pipeContextProtectionFactory);
    try {
      PipeFormat pipeFormatImpl = pipeFormatType.getConstructor(PipeContextProtectionFactory.class).newInstance(pipeContextProtectionFactory);
      Pipe pipe = new Pipe(new PipeCore(pipeName, pipeFormatImpl));
      getPipes().put(pipeName, pipe);
    } catch(InvocationTargetException e) {
      throw new NoPipeFoundException(e);
    } catch(NoSuchMethodException e) {
      throw new NoPipeFoundException(e);
    } catch(InstantiationException e) {
      throw new NoPipeFoundException(e);
    } catch(IllegalAccessException e) {
      throw new NoPipeFoundException(e);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "definePipe");
    }
  }

  public void initialize() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "initialize");
    try {
      PipeableBeanInfo beanInfo = null;
      for(Class<? extends Bean> beanType : getRegisteredBeanTypes()) {
        // load bean info
        beanInfo = PipedBeanInfoFactory.getBeanInfo(beanType);
        String[] sortedPropertyNames = beanInfo.getSortedPropertyNames().toArray(new String[0]);
        for(Pipe pipe : getPipes().values()) {
          if(Log.isDebugEnabled(getClass())) Log.debug(getClass(), "initialize", "Registering property order for bean type " + beanType + ". Sorted property names: " + StringUtils.unrollArrayForDebug(sortedPropertyNames));
          getPipeFormat(pipe).getSorter().registerFieldOrder(beanType, sortedPropertyNames);
        }
      }
      for(Pipe pipe : getPipes().values()) {
        // create engines
        getPipeFormat(pipe).createEngine();
        // set default modes
        getPipeFormat(pipe).getEngine().setMode(XStream.NO_REFERENCES);
        getPipeFormat(pipe).getEngine().autodetectAnnotations(false);
      }
      for(Class<? extends Bean> beanType : getRegisteredBeanTypes()) {
        for(Pipe pipe : getPipes().values()) {
          // process bean annotations
          getPipeFormat(pipe).getEngine().processAnnotations(beanType);
        }
      }
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "initialize");
    }
  }

  public Pipe getPipe(String pipeName) throws NoPipeFoundException {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "getPipe", pipeName);
    Pipe ret = null;
    try {
      ret = getPipes().get(pipeName);
      if(null == ret)
        throw new NoPipeFoundException("No pipe was created with name '" + pipeName + "'");
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "getPipe", ret);
    }
  }

  private BasePipeFormat getPipeFormat(Pipe pipe) {
    return (BasePipeFormat) pipe.getCore().getPipeFormat();
  }
}
