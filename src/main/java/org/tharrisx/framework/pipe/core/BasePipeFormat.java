package org.tharrisx.framework.pipe.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.PipeContext;
import org.tharrisx.framework.pipe.PipeContextProtectionFactory;
import org.tharrisx.framework.pipe.PipeFormat;
import org.tharrisx.framework.pipe.exception.InvalidPipeDataException;
import org.tharrisx.framework.pipe.exception.PipeException;
import org.tharrisx.util.log.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 * Base class for a pipe format. An instance of this class carries its own cached, pre-configured XStream library
 * instance, and this base class handles the "heavy lifting" of the actual XStream API marshalling calls.
 * 
 * @author tharrisx
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
abstract class BasePipeFormat implements PipeFormat {

  private PipeContextProtectionFactory pipeContextProtectionFactory;

  protected PipeContextProtectionFactory getPipeContextProtectionMapFactory() {
    return this.pipeContextProtectionFactory;
  }

  private final PipeBeanPropertySorter sorter = new PipeBeanPropertySorter();

  protected PipeBeanPropertySorter getSorter() {
    return this.sorter;
  }

  private XStream engine = null;

  protected XStream getEngine() {
    return this.engine;
  }

  protected void setEngine(XStream arg) {
    this.engine = arg;
  }

  protected BasePipeFormat(PipeContextProtectionFactory pipeContextProtectionMapFactory1) {
    super();
    if(Log.isEnteringEnabled(BasePipeFormat.class))
      Log.entering(BasePipeFormat.class, Log.METHOD_NAME_CONSTRUCTOR, pipeContextProtectionMapFactory1);
    try {
      this.pipeContextProtectionFactory = pipeContextProtectionMapFactory1;
    } finally {
      if(Log.isExitingEnabled(BasePipeFormat.class))
        Log.exiting(BasePipeFormat.class, Log.METHOD_NAME_CONSTRUCTOR);
    }
  }

  protected abstract void createEngine();

  protected final ReflectionProvider createPipeContextReflectionProvider() {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "createPipeContextReflectionProvider");
    ReflectionProvider ret = null;
    try {
      Sun14ReflectionProvider innerReflectionProvider = new Sun14ReflectionProvider(new FieldDictionary(getSorter()));
      ret = new PipeContextReflectionProvider(innerReflectionProvider, getPipeContextProtectionMapFactory());
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "createPipeContextReflectionProvider", ret);
    }
  }

  public final void write(Bean bean, Writer sink, PipeContext pipeContext) throws PipeException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "write", bean, sink, pipeContext);
    try {
      ((PipeableBean) bean).setPipeContext(pipeContext);
      getEngine().toXML(bean, sink);
      sink.flush();
    } catch(XStreamException e) {
      throw new PipeException(e);
    } catch(IOException e) {
      throw new PipeException(e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "write");
    }
  }

  public final Bean read(Reader source) throws PipeException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "read", source);
    Bean ret = null;
    try {
      ret = (Bean) getEngine().fromXML(source);
      ((PipeableBean) ret).setPipeContext(PipeContext.UNMARSHAL);
      return ret;
    } catch(Throwable e) {
      // bad user input here...
      throw new InvalidPipeDataException(e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "read", ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("pipeContextProtectionFactory", getPipeContextProtectionMapFactory()).append("sorter", getSorter()).append("engine", getEngine()).toString();
  }
}
