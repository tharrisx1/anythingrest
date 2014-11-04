package org.tharrisx.framework.store;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.tharrisx.framework.store.core.BeanStoreStackInfo;
import org.tharrisx.framework.store.exception.InvalidBeanPropertyValueException;
import org.tharrisx.framework.store.exception.NoSuchBeanPropertyException;
import org.tharrisx.util.DateUtils;
import org.tharrisx.util.reflect.FieldTypeRegistry;
import org.tharrisx.util.reflect.TypeStringConstructorRegistry;
import org.tharrisx.util.text.StringUtils;

/**
 * Adapts a whole host of standard bean property types. If needed in your
 * application, you can subclass this and define application-specific type to
 * string conversions.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class DefaultBeanPropertyValueAdapter implements BeanPropertyValueAdapter {

  /**
   * Adapts a specific type to a string and back again.
   * 
   * @author tharrisx
   * @since 1.0.0
   * @version 1.0.0
   */
  public static class PropertyTypeStringAdapter {

    private final Class<?> type;

    public Class<?> getType() {
      return this.type;
    }

    public PropertyTypeStringAdapter(Class<?> aType) {
      this.type = aType;
    }

    /**
     * Adapts the Object value to a String value.
     * 
     * @param info BeanStoreStackInfo
     * @param propertyValue Object
     * @return String
     */
    public String stringify(BeanStoreStackInfo info, Object propertyValue) throws InvalidBeanPropertyValueException {
      return null == propertyValue ? "null" : propertyValue.toString();
    }

    /**
     * Adapt the String value to the Type value.
     * 
     * @param info BeanStoreStackInfo
     * @param propertyValue propertyValue
     * @return Object
     * @throws NoSuchBeanPropertyException
     */
    public Object destringify(BeanStoreStackInfo info, String propertyValue) throws NoSuchBeanPropertyException {
      Object ret = null;
      try {
        Constructor<?> constructor = TypeStringConstructorRegistry.get().getConstructor(getType());
        ret = StringUtils.isEmpty(propertyValue) ? null : constructor.newInstance(propertyValue);
      } catch(Exception e) {
        throw new NoSuchBeanPropertyException(info, e);
      }
      return ret;
    }
  }

  private final Map<Class<?>, PropertyTypeStringAdapter> propertyTypeStringAdapters = new HashMap<>();

  protected Map<Class<?>, PropertyTypeStringAdapter> getTypeAdapters() {
    return this.propertyTypeStringAdapters;
  }

  /**
   * Bean property type defaults are all defined here
   */
  public DefaultBeanPropertyValueAdapter() {

    getTypeAdapters().put(boolean.class, new PropertyTypeStringAdapter(boolean.class));
    getTypeAdapters().put(Boolean.class, new PropertyTypeStringAdapter(Boolean.class));

    getTypeAdapters().put(byte.class, new PropertyTypeStringAdapter(byte.class));
    getTypeAdapters().put(Byte.class, new PropertyTypeStringAdapter(Byte.class));

    getTypeAdapters().put(short.class, new PropertyTypeStringAdapter(short.class));
    getTypeAdapters().put(Short.class, new PropertyTypeStringAdapter(Short.class));

    getTypeAdapters().put(int.class, new PropertyTypeStringAdapter(int.class));
    getTypeAdapters().put(Integer.class, new PropertyTypeStringAdapter(Integer.class));

    getTypeAdapters().put(long.class, new PropertyTypeStringAdapter(long.class));
    getTypeAdapters().put(Long.class, new PropertyTypeStringAdapter(Long.class));

    getTypeAdapters().put(float.class, new PropertyTypeStringAdapter(float.class));
    getTypeAdapters().put(Float.class, new PropertyTypeStringAdapter(Float.class));

    getTypeAdapters().put(double.class, new PropertyTypeStringAdapter(double.class));
    getTypeAdapters().put(Double.class, new PropertyTypeStringAdapter(Double.class));

    getTypeAdapters().put(char.class, new PropertyTypeStringAdapter(char.class));
    getTypeAdapters().put(Character.class, new PropertyTypeStringAdapter(Character.class));

    getTypeAdapters().put(String.class, new PropertyTypeStringAdapter(String.class) {
      @Override
      public String stringify(BeanStoreStackInfo info, Object propertyValue) {
        return (String) propertyValue;
      }

      @Override
      public Object destringify(BeanStoreStackInfo info, String propertyValue) throws NoSuchBeanPropertyException {
        return propertyValue;
      }
    });

    getTypeAdapters().put(Date.class, new PropertyTypeStringAdapter(Date.class) {
      @Override
      public String stringify(BeanStoreStackInfo info, Object propertyValue) {
        return null == propertyValue ? "null" : DateUtils.getStringFromDate((Date) propertyValue);
      }

      @Override
      public Object destringify(BeanStoreStackInfo info, String propertyValue) throws NoSuchBeanPropertyException {
        try {
          return StringUtils.isEmpty(propertyValue) ? null : DateUtils.getDateFromString(propertyValue);
        } catch(ParseException e) {
          throw new NoSuchBeanPropertyException(info, e);
        }
      }
    });

    getTypeAdapters().put(Calendar.class, new PropertyTypeStringAdapter(Calendar.class) {
      @Override
      public String stringify(BeanStoreStackInfo info, Object propertyValue) {
        return null == propertyValue ? "null" : DateUtils.getStringFromCalendar((Calendar) propertyValue);
      }

      @Override
      public Object destringify(BeanStoreStackInfo info, String propertyValue) throws NoSuchBeanPropertyException {
        try {
          return StringUtils.isEmpty(propertyValue) ? null : DateUtils.getCalendarFromString(propertyValue);
        } catch(ParseException e) {
          throw new NoSuchBeanPropertyException(info, e);
        }
      }
    });
  }

  protected Class<?> getBeanPropertyType(BeanStoreStackInfo info, String propertyName) throws NoSuchBeanPropertyException {
    try {
      return FieldTypeRegistry.get().getFieldType(info.getBeanType(), propertyName);
    } catch(NoSuchFieldException e) {
      throw new NoSuchBeanPropertyException(info, e);
    }
  }

  @Override
  public Object getAdaptedValue(BeanStoreStackInfo info, String propertyName, String propertyValue) throws NoSuchBeanPropertyException, InvalidBeanPropertyValueException {
    Class<?> beanPropertyType = getBeanPropertyType(info, propertyName);
    return getTypeAdapters().get(beanPropertyType).destringify(info, propertyValue);
  }

  @Override
  public String getStringValue(BeanStoreStackInfo info, String propertyName, Object propertyValue) throws NoSuchBeanPropertyException, InvalidBeanPropertyValueException {
    Class<?> beanPropertyType = getBeanPropertyType(info, propertyName);
    return getTypeAdapters().get(beanPropertyType).stringify(info, propertyValue);
  }
}
