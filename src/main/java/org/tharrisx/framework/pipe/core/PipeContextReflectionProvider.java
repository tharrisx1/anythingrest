package org.tharrisx.framework.pipe.core;

import java.lang.reflect.Field;
import java.util.Set;

import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.PipeContext;
import org.tharrisx.framework.pipe.PipeContextProtectionFactory;
import org.tharrisx.framework.pipe.ProtectionType;
import org.tharrisx.framework.pipe.codec.Codec;
import org.tharrisx.util.log.Log;

import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 * Wraps XStream's ReflectionProvider to perform the following security functions:
 * <ul>
 *   <li>Omit bean properties that are defined to be out of the pipe context of the call. The
 *       ProtectionType enum defines the configured pipeManager setting for each of the bean
 *       properties, while the PipeContext enum defines the context semantics of the pipeManager
 *       usage by the application. The mapping between them is defined by the pipeName's relevant
 *       PipeContextProtectionFactory.
 *   <li>Encrypt or decrypt any bean properties that define the @PipeEncryption pipeManager property, using
 *       the defined Codec implementation class. 
 * </ul>
 * 
 * @author tharrisx
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings("deprecation")
class PipeContextReflectionProvider extends ReflectionProviderWrapper {

  private PipeContextProtectionFactory pipeContextProtectionFactory = null;

  PipeContextProtectionFactory getPipeContextProtectionMapFactory() {
    return this.pipeContextProtectionFactory;
  }

  PipeContextReflectionProvider(final Sun14ReflectionProvider wrapped1, final PipeContextProtectionFactory pipeContextProtectionMapFactory1) {
    super(wrapped1);
    this.pipeContextProtectionFactory = pipeContextProtectionMapFactory1;
    if(Log.isTraceEnabled(PipeContextReflectionProvider.class)) Log.entering(PipeContextReflectionProvider.class, Log.METHOD_NAME_CONSTRUCTOR, "Created.");
  }

  // implement wrapped methods

  // interfacing to non-generic library
  @SuppressWarnings({ "rawtypes" })
  @Override
  public boolean fieldDefinedInClass(String fieldName, Class type) {
    //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "fieldDefinedInClass", fieldName, type);
    boolean ret = false;
    try {
      if(PipeableBean.class.equals(type)) {
        ret = false;
      } else {
        ret = getWrapped().fieldDefinedInClass(fieldName, type);
      }
      return ret;
    } finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "fieldDefinedInClass", ret);
    }
  }

  // interfacing to non-generic library
  @SuppressWarnings("rawtypes")
  @Override
  public Field getField(Class definedIn, String fieldName) {
    //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "getField", definedIn, fieldName);
    Field ret = null;
    try {
      ret = getWrapped().getField(definedIn, fieldName);
      return ret;
    } finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "getField", ret);
    }
  }

  // interfacing to non-generic library
  @SuppressWarnings("rawtypes")
  @Override
  public Class getFieldType(Object object, String fieldName, Class definedIn) {
    //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "getFieldType", object, fieldName, definedIn);
    Class ret = null;
    try {
      ret = getWrapped().getFieldType(object, fieldName, definedIn);
      return ret;
    } finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "getFieldType", ret);
    }
  }

  // interfacing to non-generic library
  @SuppressWarnings("rawtypes")
  @Override
  public Object newInstance(Class type) {
    //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "newInstance", type);
    Object ret = null;
    try {
      ret = getWrapped().newInstance(type);
      return ret;
    } finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "newInstance", ret);
    }
  }

  @Override
  public void visitSerializableFields(final Object object, final Visitor visitor) {
    //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "visitSerializableFields", object, visitor);
    try {
      Class<?> type = object.getClass();
      if(!Bean.class.isInstance(object)) {
        getWrapped().visitSerializableFields(object, visitor);
      } else {
        PipeContext pipeContext = getPipeContext((Bean) object);
        getWrapped().visitSerializableFields(object, new BeanVisitor(visitor, type, pipeContext, getPipeContextProtectionMapFactory()));
      }
    } finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "visitSerializableFields");
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  // interfacing to non-generic library
  @Override
  public void writeField(Object object, String fieldName, Object value, Class definedIn) {
    //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "writeField", object, fieldName, value, definedIn);
    try {
      Object newValue = value;
      if(Bean.class.isInstance(object)) {
        PipeableBeanInfo pipeableBeanInfo = PipedBeanInfoFactory.getBeanInfo((Class<? extends Bean>) object.getClass());
        // implement automatic encryption/decryption
        if(null != pipeableBeanInfo.getEncryption(fieldName)) {
          Codec codec = pipeableBeanInfo.getEncryption(fieldName).newInstance();
          if(PipeContext.UNMARSHAL.equals(getPipeContext((Bean) object))) {
            newValue = codec.decode(value);
          } else {
            newValue = codec.encode(value);
          }
        }
      }
      getWrapped().writeField(object, fieldName, newValue, definedIn);
    } catch(InstantiationException e) {
      Log.error(getClass(), "writeField", "The specified Codec class could not be instantiated with the default constructor.", e);
    } catch(IllegalAccessException e) {
      Log.error(getClass(), "writeField", "The specified Codec class was not publicly constructable.", e);
    } finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "writeField");
    }
  }

  private PipeContext getPipeContext(Bean bean) {
    //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "getPipeContext", bean);
    PipeContext ret = null;
    try {
      try {
        ret = ((PipeableBean) bean).getPipeContext();
      } catch(ClassCastException e) {
        Log.fatal(getClass(), "getPipeContext", "The bean " + bean + " of type " + bean.getClass() + " was not found to decend from Bean! Rethink your strategy.", e);
      }
      return ret;
    } finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "getPipeContext", ret);
    }
  }

  @SuppressWarnings("unqualified-field-access")
  // interfacing to non-generic library
  private Sun14ReflectionProvider getWrapped() {
    return ((Sun14ReflectionProvider) wrapped);
  }

  @SuppressWarnings("unqualified-field-access")
  // interfacing to non-generic library
  @Override
  public String toString() {
    return new ToStringBuilder(this).appendSuper(wrapped.toString()).append("pipeContextProtectionFactory", this.getPipeContextProtectionMapFactory()).toString();
  }

  private static class BeanVisitor implements Visitor {

    private final Visitor wrappedVisitor;
    private final Class<? extends Bean> beanType;
    private final PipeContext pipeContext;
    private final PipeContextProtectionFactory pipeContextProtectionFactory;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    // interfacing to non-generic library
    BeanVisitor(final Visitor wrappedVisitor1, final Class beanType1, final PipeContext pipeContext1, final PipeContextProtectionFactory pipeContextProtectionMapFactory1) {
      this.wrappedVisitor = wrappedVisitor1;
      this.beanType = beanType1;
      this.pipeContext = pipeContext1;
      this.pipeContextProtectionFactory = pipeContextProtectionMapFactory1;
    }

    @SuppressWarnings("rawtypes")
    @Override
    // interfacing to non-generic library
    public void visit(String name, Class type, Class definedIn, Object value) {
      //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "visit", name, type, definedIn, value);
      //try {
      if(isBeanPropertyAllowed(value, name)) {
        this.wrappedVisitor.visit(name, type, definedIn, value);
      }
      //} finally {
      //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "visit");
      //}
    }

    private boolean isBeanPropertyAllowed(Object object, String propertyName) {
      //if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "isBeanPropertyAllowed", propertyName);
      boolean ret = true;
      try {
        PipeableBeanInfo pipeableBeanInfo = PipedBeanInfoFactory.getBeanInfo(this.beanType);
        if(null == pipeableBeanInfo) {
          String msg = "No metadata found for bean type: " + this.beanType;
          Log.fatal(getClass(), "isBeanPropertyInContext", msg);
          throw new WebApplicationException(new RuntimeException(msg));
        }
        ProtectionType propertyProtectionType = pipeableBeanInfo.getProtectionType(propertyName);
        if(null == propertyProtectionType) {
          Log.warn(getClass(), "isBeanPropertyInContext", "No PropertyProtectionType found for bean property: " + this.beanType + "." + propertyName);
          propertyProtectionType = ProtectionType.PUBLIC; // default
        }
        // $$$ must pass user credentials in through PipeableBean, define credentials
        // if(propertyProtectionType.getProtectionCheck().isAuthorized(object, userId));
        Set<ProtectionType> contextProtectionMap = this.pipeContextProtectionFactory.getPipeContextProtectionMap().get(this.pipeContext);
        if(null == contextProtectionMap) {
          Log.warn(getClass(), "isBeanPropertyInContext", "No PipeContextProtectionMap found for pipe context " + this.pipeContext + " for property " + this.beanType + "." + propertyName + ".");
          ret = false; // skipping
        } else {
          ret = contextProtectionMap.contains(propertyProtectionType);
          if(Log.isDebugEnabled(getClass())) Log.debug(getClass(), "isBeanPropertyAllowed", "Property " + this.beanType + "." + propertyName + "' is " + (ret ? "" : "NOT ") + "allowed for context " + this.pipeContext);
        }
        return ret;
      } finally {
        //if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "isBeanPropertyAllowed", ret);
      }
    }
  }
}
