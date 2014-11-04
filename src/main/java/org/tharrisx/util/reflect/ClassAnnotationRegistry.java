package org.tharrisx.util.reflect;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.util.log.Log;

/**
 * A cache of class annotation information, to make class annotation
 * introspection a much more efficient activity.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class ClassAnnotationRegistry {

  static class ClassRegistryKey {

    private final Class<?> type;

    Class<?> getType() {
      return this.type;
    }

    private final Class<? extends Annotation> annotation;

    Class<? extends Annotation> getAnnotation() {
      return this.annotation;
    }

    ClassRegistryKey(Class<?> type1, Class<? extends Annotation> annotation1) {
      this.type = type1;
      this.annotation = annotation1;
    }

    @Override
    public boolean equals(final Object other) {
      if(this == other)
        return true;
      if(!(other instanceof ClassRegistryKey))
        return false;
      ClassRegistryKey castOther = (ClassRegistryKey) other;
      return new EqualsBuilder().append(getType(), castOther.getType()).append(getAnnotation(), castOther.getAnnotation()).isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(-248583183, -830812089).append(getType()).append(getAnnotation()).toHashCode();
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("type", getType()).append("annotation", getAnnotation()).toString();
    }
  }

  private static final String NL = System.getProperty("line.separator");

  private static ClassAnnotationRegistry instance = null;

  /**
   * Singleton factory method
   * 
   * @return MethodRegistry
   */
  public static ClassAnnotationRegistry get() {
    if(null == instance)
      instance = new ClassAnnotationRegistry();
    return instance;
  }

  private Map<ClassRegistryKey, Annotation> registry = null;

  private Map<ClassRegistryKey, Annotation> getRegistry() {
    return this.registry;
  }

  private ClassAnnotationRegistry() {
    this.registry = new HashMap<>(5);
    if(Log.isTraceEnabled(ClassAnnotationRegistry.class))
      Log.trace(ClassAnnotationRegistry.class, Log.METHOD_NAME_CONSTRUCTOR, "Created.");
  }

  @SuppressWarnings("unchecked")
  public <T extends Annotation> T getClassAnnotation(Class<?> targetType, Class<T> annotation) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "getClassAnnotation", targetType, annotation);
    T ret = null;
    try {
      ClassRegistryKey key = new ClassRegistryKey(targetType, annotation);
      ret = (T) getRegistry().get(key);
      if(null == ret && targetType.isAnnotationPresent(annotation)) {
        ret = targetType.getAnnotation(annotation);
        getRegistry().put(key, ret);
      }
      return ret;
    } finally {
      if(Log.isEnteringEnabled(getClass()))
        Log.exiting(getClass(), "getClassAnnotation", ret);
    }
  }

  @Override
  public String toString() {
    StringBuilder retBuf = new StringBuilder("ClassAnnotationRegistry Dump:" + NL);
    for(Map.Entry<ClassRegistryKey, Annotation> classEntry : getRegistry().entrySet()) {
      retBuf.append("  ").append(classEntry.getKey()).append(" -> ").append(classEntry.getValue()).append(NL);
    }
    return retBuf.toString();
  }
}
