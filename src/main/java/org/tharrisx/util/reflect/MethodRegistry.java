package org.tharrisx.util.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.tharrisx.util.log.Log;

/**
 * A cache of method reflection information, to make method introspection a much
 * more efficient activity.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class MethodRegistry {

  /**
   * An enum that defines all the various bean-like method types that the method
   * registry handles.
   */
  public enum MethodType {
    ENTITY_ACCESSOR, ENTITY_COLLECTION_ACCESSOR, ENTITY_MUTATOR, ENTITY_COLLECTION_MUTATOR
  }

  /**
   * Used internally as mapping keys for method information.
   */
  private static class MethodKey {

    private Class<?> clazz = null;

    private Class<?> getClazz() {
      return this.clazz;
    }

    private MethodType type = null;

    private MethodType getType() {
      return this.type;
    }

    MethodKey(Class<?> aClazz, MethodType aType) {
      this.clazz = aClazz;
      this.type = aType;
    }

    @Override
    public boolean equals(Object o) {
      if(!(o instanceof MethodKey))
        return false;
      if(this == o)
        return true;
      MethodKey rhs = (MethodKey) o;
      return new EqualsBuilder().append(getClazz(), rhs.getClazz()).append(getType(), rhs.getType()).isEquals();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder().append(getClazz()).append(getType()).toHashCode();
    }

    @Override
    public String toString() {
      return "Entity: " + getClazz().getCanonicalName() + " MethodType: " + getType();
    }
  }

  private static final String NL = System.getProperty("line.separator");

  private static MethodRegistry instance = null;

  /**
   * Singleton factory method
   * 
   * @return MethodRegistry
   */
  public static MethodRegistry get() {
    if(null == instance)
      instance = new MethodRegistry();
    return instance;
  }

  private Map<Class<?>, Map<MethodKey, Set<Method>>> registry = null;

  private Map<Class<?>, Map<MethodKey, Set<Method>>> getRegistry() {
    return this.registry;
  }

  private MethodRegistry() {
    this.registry = new HashMap<>(5);
    if(Log.isTraceEnabled(MethodRegistry.class))
      Log.trace(MethodRegistry.class, Log.METHOD_NAME_CONSTRUCTOR, "Created.");
  }

  /**
   * From the targetType class, retrieve all the methods of the particular type
   * that involve the involvedType. The involvement depends on the method type.
   * 
   * @param targetType Class<?>
   * @param involvedType Class<?>
   * @param methodType MethodType
   * @return Set<Method>
   */
  public Set<Method> getMethods(Class<?> targetType, Class<?> involvedType, MethodType methodType) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "getMethods", targetType, involvedType, methodType);
    Set<Method> ret = null;
    try {
      if((null == targetType) || (null == involvedType) || (null == methodType)) {
        ret = new HashSet<>(0);
      } else {
        MethodKey key = new MethodKey(involvedType, methodType);
        ret = getClassMap(targetType).get(key);
        if(null == ret) {
          switch(methodType) {
          case ENTITY_ACCESSOR:
            ret = ReflectionUtils.getMethodsReturningType(targetType, involvedType);
            break;
          case ENTITY_COLLECTION_ACCESSOR:
            ret = ReflectionUtils.getMethodsReturningCollectionOfType(targetType, involvedType);
            break;
          case ENTITY_MUTATOR:
            ret = ReflectionUtils.getMethodsTakingOneParameterOfType(targetType, involvedType);
            break;
          case ENTITY_COLLECTION_MUTATOR:
            ret = ReflectionUtils.getMethodsTakingOneParameterOfCollectionOfType(targetType, involvedType);
            break;
          default:
            break;
          }
          if(Log.isTraceEnabled(getClass()))
            Log.trace(getClass(), "getMethods", "Cache MISS on targetType=" + targetType + ", involvedType=" + involvedType + ", methodType=" + methodType + ". Adding to cache...");
          getClassMap(targetType).put(key, ret);
        } else {
          if(Log.isTraceEnabled(getClass()))
            Log.trace(getClass(), "getMethods", "Cache HIT on targetType=" + targetType + ", involvedType=" + involvedType + ", methodType=" + methodType + '.');
        }
      }
      return ret;
    } finally {
      if(Log.isEnteringEnabled(getClass()))
        Log.exiting(getClass(), "getMethods", ret);
    }
  }

  @Override
  public String toString() {
    StringBuilder retBuf = new StringBuilder("MethodRegistry Dump:" + NL);
    for(Map.Entry<Class<?>, Map<MethodKey, Set<Method>>> classEntry : getRegistry().entrySet()) {
      retBuf.append("  ").append(classEntry.getKey().getName()).append(" ->").append(NL);
      for(Map.Entry<MethodKey, Set<Method>> fieldsEntry : classEntry.getValue().entrySet()) {
        retBuf.append("    ").append(fieldsEntry.getKey()).append(" -> ");
        retBuf.append(fieldsEntry.getValue()).append(NL);
      }
    }
    return retBuf.toString();
  }

  private Map<MethodKey, Set<Method>> getClassMap(Class<?> baseClass) {
    Map<MethodKey, Set<Method>> ret = getRegistry().get(baseClass);
    if(null == ret) {
      ret = new HashMap<>(5);
      getRegistry().put(baseClass, ret);
    }
    return ret;
  }
}
