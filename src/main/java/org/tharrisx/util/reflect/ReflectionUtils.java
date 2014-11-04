package org.tharrisx.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.tharrisx.util.log.Log;

/**
 * Some reflection helper methods.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class ReflectionUtils {

  public static Class<?> getFieldType(Class<?> targetType, String fieldName) throws SecurityException, NoSuchFieldException {
    return targetType.getField(fieldName).getType();
  }

  /**
   * Get the value of a particular field from a target object.
   * 
   * @param target Object
   * @param field Field
   * @return Object
   */
  public static Object getFieldValue(Object target, Field field) {
    try {
      field.setAccessible(true);
      return field.get(target);
    } catch(Throwable e) {
      throw new ReflectionException(e);
    }
  }

  public static void setFieldValue(Object target, Field field, Object value) {
    try {
      field.setAccessible(true);
      field.set(target, value);
    } catch(Throwable e) {
      throw new ReflectionException(e);
    }
  }

  /**
   * Using "bean" semantics, construct a getter name for a field name.
   * 
   * @param fieldName String
   * @return String
   */
  public static String makeAccessorMethodName(String fieldName) {
    return "get" + capitalizeFirstLetter(fieldName);
  }

  /**
   * Using "bean" semantics, construct a setter name for a field name.
   * 
   * @param fieldName String
   * @return String
   */
  public static String makeMutatorMethodName(String fieldName) {
    return "set" + capitalizeFirstLetter(fieldName);
  }

  /**
   * Using "bean" semantics, take a getter/setter method name, and construct the
   * field name.
   * 
   * @param accessorName String
   * @return String
   */
  public static String makeFieldNameFromMethodName(String accessorName) {
    return decapitalizeFirstLetter(accessorName.substring(accessorName.startsWith("is") ? 2 : 3));
  }

  /**
   * True only if this method has a single boolean argument.
   * 
   * @param target Class<?>
   * @param methodName String
   * @return boolean
   */
  public static boolean doesMethodTakeOneBooleanArgument(Class<?> target, String methodName) {
    try {
      target.getMethod(methodName, boolean.class);
    } catch(NoSuchMethodException e) {
      return false;
    }
    return true;
  }

  static String capitalizeFirstLetter(String fieldName) {
    String ret = fieldName;
    ret = ret.substring(0, 1).toUpperCase() + ret.substring(1);
    return ret;
  }

  static String decapitalizeFirstLetter(String fieldName) {
    String ret = fieldName;
    ret = ret.substring(0, 1).toLowerCase() + ret.substring(1);
    return ret;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  static Set<FieldAnnotation<? extends Annotation>> getFieldsAnnotatedAs(final Class<?> targetType, final Class<? extends Annotation> annotationType) {
    if(Log.isEnteringEnabled(ReflectionUtils.class))
      Log.entering(ReflectionUtils.class, "getFieldsAnnotatedAs", targetType, annotationType);
    Set<FieldAnnotation<?>> ret = new HashSet<>();
    try {
      Set<Field> fields = new HashSet<>();
      Class<?> currentType = targetType;
      while(null != currentType) {
        fields.addAll(Arrays.asList(currentType.getDeclaredFields()));
        currentType = currentType.getSuperclass();
      }
      for(Field field : fields) {
        if(null != field.getAnnotation(annotationType)) {
          ret.add(new FieldAnnotation(field, field.getAnnotation(annotationType)));
        }
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(ReflectionUtils.class))
        Log.exiting(ReflectionUtils.class, "getFieldsAnnotatedAs", ret);
    }
  }

  static boolean doesMethodReturnType(Method m, Type type) {
    return type.equals(m.getReturnType());
  }

  static boolean doesMethodReturnCollectionOfType(Method m, Type retType) {
    Type genericReturnType = m.getGenericReturnType();
    if(!(genericReturnType instanceof ParameterizedType)) {
      return false;
    }
    Type[] typeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
    if(0 == typeArguments.length) {
      return false;
    }
    return typeArguments[0].equals(retType);
  }

  static boolean doesMethodTakeOneParameterOfType(Method m, Class<?> paramType) {
    Class<?>[] paramTypes = m.getParameterTypes();
    if(1 != paramTypes.length) {
      return false;
    }
    return paramType.equals(paramTypes[0]);
  }

  static boolean doesMethodTakeOneParameterOfCollectionOfType(Method m, Class<?> paramType) {
    Type[] genericParamTypes = m.getGenericParameterTypes();
    if(1 != genericParamTypes.length) {
      return false;
    }
    Type[] typeArguments = ((ParameterizedType) genericParamTypes[0]).getActualTypeArguments();
    if(1 != typeArguments.length) {
      return false;
    }
    return paramType.equals(typeArguments[0]);
  }

  static Set<Method> getMethodsReturningType(Class<?> target, Type retType) {
    Set<Method> ret = new HashSet<>(1);
    for(Method method : target.getMethods()) {
      if(doesMethodReturnType(method, retType) && ('$' != method.getName().charAt(0))) {
        ret.add(method);
      }
    }
    return ret;
  }

  static Set<Method> getMethodsReturningCollectionOfType(Class<?> target, Type retType) {
    Set<Method> ret = new HashSet<>(1);
    for(Method method : target.getMethods()) {
      if(doesMethodReturnCollectionOfType(method, retType) && ('$' != method.getName().charAt(0))) {
        ret.add(method);
      }
    }
    return ret;
  }

  static Set<Method> getMethodsTakingOneParameterOfType(Class<?> target, Class<?> paramType) {
    Set<Method> ret = new HashSet<>(1);
    for(Method method : target.getMethods()) {
      if(doesMethodTakeOneParameterOfType(method, paramType) && ('$' != method.getName().charAt(0))) {
        ret.add(method);
      }
    }
    return ret;
  }

  static Set<Method> getMethodsTakingOneParameterOfCollectionOfType(Class<?> target, Class<?> paramType) {
    Set<Method> ret = new HashSet<>(1);
    for(Method method : target.getMethods()) {
      if(doesMethodTakeOneParameterOfCollectionOfType(method, paramType) && ('$' != method.getName().charAt(0))) {
        ret.add(method);
      }
    }
    return ret;
  }

  private ReflectionUtils() {
    // private constructor protects this from being instantiated.
  }
}
