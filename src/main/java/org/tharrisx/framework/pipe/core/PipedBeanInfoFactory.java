package org.tharrisx.framework.pipe.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.annotations.PipeEncryption;
import org.tharrisx.framework.pipe.annotations.PipePropertyOrder;
import org.tharrisx.framework.pipe.annotations.PipeProtection;
import org.tharrisx.util.log.Log;
import org.tharrisx.util.reflect.FieldAnnotation;
import org.tharrisx.util.reflect.FieldAnnotationRegistry;

public class PipedBeanInfoFactory {

  private static final Map<Class<? extends Bean>, PipeableBeanInfo> beanInfoCache = new HashMap<>();

  static PipeableBeanInfo getBeanInfo(Class<? extends Bean> beanType) {
    //if(Log.isEnteringEnabled(PipedBeanInfoFactory.class)) Log.entering(PipedBeanInfoFactory.class, "getBeanInfo", beanType);
    PipeableBeanInfo ret = null;
    //try {
    ret = beanInfoCache.get(beanType);
    if(null == ret) {
      ret = loadBeanInfo(beanType);
      beanInfoCache.put(beanType, ret);
    }
    return ret;
    //} finally {
    //if(Log.isEnteringEnabled(PipedBeanInfoFactory.class)) Log.exiting(PipedBeanInfoFactory.class, "getBeanInfo", ret);
    //}
  }

  @SuppressWarnings("unchecked")
  private static PipeableBeanInfo loadBeanInfo(Class<? extends Bean> beanType) {
    if(Log.isEnteringEnabled(PipedBeanInfoFactory.class))
      Log.entering(PipedBeanInfoFactory.class, "loadBeanInfo", beanType);
    PipeableBeanInfo ret = null;
    try {
      ret = new PipeableBeanInfo();
      Set<FieldAnnotation<? extends Annotation>> fieldAnnotations = null;
      fieldAnnotations = FieldAnnotationRegistry.get().getAnnotatedFields(beanType, PipePropertyOrder.class);
      for(FieldAnnotation<? extends Annotation> fieldAnnotation : fieldAnnotations) {
        ret.definePropertyOrder(fieldAnnotation.getField().getName(),
            new PipeableBeanInfo.PipePropertyOrderInfo(fieldAnnotation.getField().getName(), ((PipePropertyOrder) fieldAnnotation.getAnnotation()).value()));
      }
      fieldAnnotations = FieldAnnotationRegistry.get().getAnnotatedFields(beanType, PipeProtection.class);
      for(FieldAnnotation<? extends Annotation> fieldAnnotation : fieldAnnotations) {
        ret.defineProtection(fieldAnnotation.getField().getName(), new PipeableBeanInfo.ProtectionInfo(((PipeProtection) fieldAnnotation.getAnnotation()).value()));
      }
      fieldAnnotations = FieldAnnotationRegistry.get().getAnnotatedFields(beanType, PipeEncryption.class);
      for(FieldAnnotation<? extends Annotation> fieldAnnotation : FieldAnnotationRegistry.get().getAnnotatedFields(beanType, PipeEncryption.class)) {
        ret.defineEncryption(fieldAnnotation.getField().getName(), new PipeableBeanInfo.EncryptionInfo(((PipeEncryption) fieldAnnotation.getAnnotation()).value()));
      }
      Set<Field> fields = new HashSet<>();
      Class<? extends Bean> currentType = beanType;
      while(null != currentType) {
        for(Field field : currentType.getDeclaredFields()) {
          if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
            fields.add(field);
          }
        }
        currentType = (Class<? extends Bean>) currentType.getSuperclass();
      }
      for(Field field : fields) {
        if(!ret.getSortedPropertyNames().contains(field.getName())) {
          ret.definePropertyOrder(field.getName(), new PipeableBeanInfo.PipePropertyOrderInfo(field.getName(), 0.0));
        }
      }
      if(Log.isDebugEnabled(PipedBeanInfoFactory.class)) {
        Log.debug(PipedBeanInfoFactory.class, "loadBeanInfo", "Bean Pipe Metadata Loaded for " + beanType);
        Log.debug(PipedBeanInfoFactory.class, "loadBeanInfo", "  protectionMap=" + ret.protectionMap);
        Log.debug(PipedBeanInfoFactory.class, "loadBeanInfo", "  encryptionMap=" + ret.encryptionMap);
        Log.debug(PipedBeanInfoFactory.class, "loadBeanInfo", "  propertyOrderSet=" + ret.propertyOrderSet);
      }
      return ret;
    } finally {
      if(Log.isEnteringEnabled(PipedBeanInfoFactory.class))
        Log.exiting(PipedBeanInfoFactory.class, "loadBeanInfo", ret);
    }
  }
}
