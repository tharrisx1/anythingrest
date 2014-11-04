package org.tharrisx.util.reflect;

import java.util.HashMap;
import java.util.Map;

import org.tharrisx.util.log.Log;

/**
 * A cache of field type information, to make field type introspection a much
 * more efficient activity.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class FieldTypeRegistry {

  private static FieldTypeRegistry instance = null;

  /**
   * Singleton factory method
   * 
   * @return FieldAnnotationRegistry
   */
  public static FieldTypeRegistry get() {
    if(null == instance)
      instance = new FieldTypeRegistry();
    return instance;
  }

  private final Map<Class<?>, Map<String, Class<?>>> registry;

  private Map<Class<?>, Map<String, Class<?>>> getRegistry() {
    return this.registry;
  }

  private FieldTypeRegistry() {
    this.registry = new HashMap<>();
    if(Log.isTraceEnabled(FieldTypeRegistry.class))
      Log.trace(FieldTypeRegistry.class, Log.METHOD_NAME_CONSTRUCTOR, "Created.");
  }

  private Map<String, Class<?>> getClassFieldMap(Class<?> targetType) {
    Map<String, Class<?>> ret = getRegistry().get(targetType);
    if(null == ret) {
      ret = new HashMap<>();
      getRegistry().put(targetType, ret);
    }
    return ret;
  }

  /**
   * Call this method to lookup the a field's type, using the cache.
   * 
   * @param targetType Class<?>
   * @param fieldName Class<?>
   * @return Class<?>
   * @throws NoSuchFieldException
   */
  public Class<?> getFieldType(Class<?> targetType, String fieldName) throws NoSuchFieldException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "getFieldType", targetType, fieldName);
    Class<?> ret = null;
    try {
      if((null == targetType) || (null == fieldName)) {
        throw new IllegalArgumentException("null arguments given to method.");
      }
      Map<String, Class<?>> fieldMap = getClassFieldMap(targetType);
      ret = fieldMap.get(fieldName);
      if(null == ret) {
        // perform the actual reflection
        try {
          ret = ReflectionUtils.getFieldType(targetType, fieldName);
        } catch(SecurityException e) {
          throw new NoSuchFieldException("targetType=" + targetType + ", fieldName=" + fieldName);
        }
        fieldMap.put(fieldName, ret);
      }
      return ret;
    } finally {
      if(Log.isEnteringEnabled(getClass()))
        Log.exiting(getClass(), "getFieldType", ret);
    }
  }

  private static final String NL = System.getProperty("line.separator");

  @Override
  public String toString() {
    StringBuilder retBuf = new StringBuilder("FieldTypeRegistry Dump:" + NL);
    for(Map.Entry<Class<?>, Map<String, Class<?>>> classEntry : getRegistry().entrySet()) {
      retBuf.append("  ").append(classEntry.getKey().getName()).append(" ->").append(NL);
      for(Map.Entry<String, Class<?>> fieldsEntry : classEntry.getValue().entrySet()) {
        retBuf.append("    ").append(fieldsEntry.getKey()).append(" -> ");
        retBuf.append(fieldsEntry.getValue().getName()).append(NL);
      }
    }
    return retBuf.toString();
  }
}
