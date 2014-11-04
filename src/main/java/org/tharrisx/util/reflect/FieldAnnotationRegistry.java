package org.tharrisx.util.reflect;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.tharrisx.util.log.Log;

/**
 * A cache of field annotation information, to make field annotation
 * introspection a much more efficient activity.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class FieldAnnotationRegistry {

  private static FieldAnnotationRegistry instance = null;

  /**
   * Singleton factory method
   * 
   * @return FieldAnnotationRegistry
   */
  public static FieldAnnotationRegistry get() {
    if(null == instance)
      instance = new FieldAnnotationRegistry();
    return instance;
  }

  private final Map<Class<?>, Map<Class<? extends Annotation>, Set<FieldAnnotation<? extends Annotation>>>> registry;

  private Map<Class<?>, Map<Class<? extends Annotation>, Set<FieldAnnotation<? extends Annotation>>>> getRegistry() {
    return this.registry;
  }

  private FieldAnnotationRegistry() {
    this.registry = new HashMap<>();
    if(Log.isTraceEnabled(FieldAnnotationRegistry.class))
      Log.trace(FieldAnnotationRegistry.class, Log.METHOD_NAME_CONSTRUCTOR, "Created.");
  }

  private Map<Class<? extends Annotation>, Set<FieldAnnotation<? extends Annotation>>> getAnnotationMap(Class<?> targetType) {
    Map<Class<? extends Annotation>, Set<FieldAnnotation<? extends Annotation>>> ret = getRegistry().get(targetType);
    if(null == ret) {
      ret = new HashMap<>();
      getRegistry().put(targetType, ret);
    }
    return ret;
  }

  /**
   * For the targetType, retrieve all the fields that, at runtime, are annotated
   * by the annotation.
   * 
   * @param targetType Class<?>
   * @param annotation Class<? extends Annotation>
   * @return Set<Field>
   */
  public Set<FieldAnnotation<? extends Annotation>> getAnnotatedFields(final Class<?> targetType, final Class<? extends Annotation> annotation) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "getFieldsAnnotatedAs", targetType, annotation);
    Set<FieldAnnotation<? extends Annotation>> ret = null;
    try {
      if((null == targetType) || (null == annotation)) {
        throw new IllegalArgumentException("null arguments given to method.");
      }
      ret = getAnnotationMap(targetType).get(annotation);
      if(null == ret) {
        // this target type and annotation type have not been loaded yet. Cache
        // miss. Load all fields...
        ret = new HashSet<>();
        for(FieldAnnotation<?> field : ReflectionUtils.getFieldsAnnotatedAs(targetType, annotation)) {
          ret.add(field);
        }
        getAnnotationMap(targetType).put(annotation, ret);
        if(Log.isTraceEnabled(getClass()))
          Log.trace(getClass(), "getFieldsAnnotatedAs", "Cache MISS on targetType=" + targetType + " and annotation=" + annotation + ". Added to cache.");
      } else {
        if(Log.isTraceEnabled(getClass()))
          Log.trace(getClass(), "getFieldsAnnotatedAs", "Cache HIT on targetType=" + targetType + " and annotation=" + annotation + '.');
      }
      return ret;
    } finally {
      if(Log.isEnteringEnabled(getClass()))
        Log.exiting(getClass(), "getFieldsAnnotatedAs", ret);
    }
  }

  private static final String NL = System.getProperty("line.separator");

  @Override
  public String toString() {
    StringBuilder retBuf = new StringBuilder("FieldAnnotationRegistry Dump:" + NL);
    for(Map.Entry<Class<?>, Map<Class<? extends Annotation>, Set<FieldAnnotation<? extends Annotation>>>> classEntry : getRegistry().entrySet()) {
      retBuf.append("  ").append(classEntry.getKey().getName()).append(" ->").append(NL);
      for(Map.Entry<Class<? extends Annotation>, Set<FieldAnnotation<? extends Annotation>>> fieldsEntry : classEntry.getValue().entrySet()) {
        retBuf.append("    ").append(fieldsEntry.getKey().getName()).append(" -> ");
        retBuf.append(fieldsEntry.getValue()).append(NL);
      }
    }
    return retBuf.toString();
  }
}
