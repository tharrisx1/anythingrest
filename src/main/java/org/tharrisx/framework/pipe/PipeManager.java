package org.tharrisx.framework.pipe;

import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.core.JsonPipeFormat;
import org.tharrisx.framework.pipe.core.PipeManagerCore;
import org.tharrisx.framework.pipe.core.XmlPipeFormat;
import org.tharrisx.framework.pipe.exception.InvalidBeanTypeException;
import org.tharrisx.framework.pipe.exception.NoPipeFoundException;

/**
 * Main Facade class for the PipeManager. All API calls should be made through a cached instance of this class.
 * The PipeManager is hard-wired to operate using the XStream library. Beans will be required to annotate their
 * beans using the XStream annotations library, along with our own annotations, as below. There is no standard bean
 * pipeManager binding language or API, so it's difficult to avoid tying this framework directly to the XStream
 * library's API without duplicating it. So, we happily expose it.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public final class PipeManager {

  public static final Class<? extends PipeFormat> PIPE_FORMAT_XML = XmlPipeFormat.class;
  public static final Class<? extends PipeFormat> PIPE_FORMAT_JSON = JsonPipeFormat.class;

  private final PipeManagerCore core;

  private PipeManagerCore getCore() {
    return this.core;
  }

  public PipeManager() {
    this.core = new PipeManagerCore();
  }

  /**
   * Call this before initialization to register each application bean class.
   * @param beanClass Class<? extends Bean> The class of application bean
   * @throws InvalidBeanTypeException if the class specified is not found or not usable due to programming errors
   */
  public void registerBean(final Class<? extends Bean> beanClass) throws InvalidBeanTypeException {
    getCore().registerBean(beanClass);
  }

  /**
   * Call this during initialization to create a pipe.
   * @param pipeName String Key that will be used to refer to the configured pipeName
   * @param pipeFormat Class<? extends AbstractTransportFormat> The class of one of the PipeFormat implementations
   * @throws NoPipeFoundException if the class specified is not found or not usable due to programming errors
   */
  public void definePipe(final String pipeName, final Class<? extends PipeFormat> pipeFormat) throws NoPipeFoundException {
    getCore().definePipe(pipeName, pipeFormat);
  }

  /**
   * Call this during initialization to create a pipe.
   * @param pipeName String Key that will be used to refer to the configured pipe
   * @param pipeFormat Class<? extends PipeFormat> The class of one of the PipeFormat implementations
   * @param pipeContextProtectionFactory PipeContextProtectionFactory A custom security definition
   * @throws NoPipeFoundException if the class specified is not found or not usable due to programming errors
   */
  public void definePipe(final String pipeName, final Class<? extends PipeFormat> pipeFormat, final PipeContextProtectionFactory pipeContextProtectionFactory) throws NoPipeFoundException {
    getCore().definePipe(pipeName, pipeFormat, pipeContextProtectionFactory);
  }

  /**
   * Get the previously defined Pipe by its name,
   * @param pipeName String
   * @return Pipe
   * @throws NoPipeFoundException
   */
  public Pipe getPipe(final String pipeName) throws NoPipeFoundException {
    return getCore().getPipe(pipeName);
  }

  /**
   * Call this after registering all formats and beans with the Transport engine.
   */
  public void initialize() {
    getCore().initialize();
  }
}
