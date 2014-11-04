package org.tharrisx.util.reflect;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.tharrisx.util.log.Log;

public class TypeStringConstructorRegistry {

  private static TypeStringConstructorRegistry instance = null;

  public static TypeStringConstructorRegistry get() {
    if(null == instance)
      instance = new TypeStringConstructorRegistry();
    return instance;
  }

  private final Map<Class<?>, Constructor<?>> registry;

  private Map<Class<?>, Constructor<?>> getRegistry() {
    return this.registry;
  }

  private TypeStringConstructorRegistry() {
    this.registry = new HashMap<>();
    if(Log.isTraceEnabled(TypeStringConstructorRegistry.class))
      Log.trace(TypeStringConstructorRegistry.class, Log.METHOD_NAME_CONSTRUCTOR, "Created.");
  }

  /**
   * Call this method to lookup the a field's type, using the cache.
   * 
   * @param targetType
   * @param fieldName
   * @return Constructor<?>
   * @throws NoSuchFieldException
   */
  public Constructor<?> getConstructor(Class<?> targetType) throws NoSuchMethodException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "getConstructor", targetType);
    Constructor<?> ret = null;
    try {
      if(null == targetType)
        throw new IllegalArgumentException("null arguments given to method.");
      ret = getRegistry().get(targetType);
      if(null == ret) {
        // perform the actual reflection
        try {
          ret = targetType.getConstructor(String.class);
        } catch(SecurityException e) {
          throw new NoSuchMethodException("targetType=" + targetType);
        }
        getRegistry().put(targetType, ret);
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "getConstructor", ret);
    }
  }

  private static final String NL = System.getProperty("line.separator");

  /**
   * Verbose toString() formatting: targetType - Class<?> constructor -
   * Constructor
   * 
   * @return String
   */
  @Override
  public String toString() {
    StringBuilder retBuf = new StringBuilder("TypeStringConstructorRegistry Dump:" + NL);
    for(Map.Entry<Class<?>, Constructor<?>> entry : getRegistry().entrySet()) {
      retBuf.append("  ").append(entry.getKey().getName()).append(" -> ").append(entry.getValue().toString()).append(NL);
    }
    return retBuf.toString();
  }
}
