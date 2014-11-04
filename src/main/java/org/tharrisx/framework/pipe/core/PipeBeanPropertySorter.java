package org.tharrisx.framework.pipe.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

import org.tharrisx.util.log.Log;

import com.thoughtworks.xstream.converters.reflection.FieldKey;
import com.thoughtworks.xstream.converters.reflection.FieldKeySorter;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;

/**
 * Wraps XStream's FieldKeySorter to provide the bean field orderings.
 * 
 * @author tharrisx
 * @version 1.0.0
 * @since 1.0.0
 */
public class PipeBeanPropertySorter implements FieldKeySorter {

  private final Map<Class<?>, Object> map = new WeakHashMap<>();

  private Map<Class<?>, Object> getMap() {
    return this.map;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Map sort(Class type, Map keyedByFieldKey) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "sort", type, keyedByFieldKey);
    Map ret = null;
    try {
      if(getMap().containsKey(type)) {
        Map result = new OrderRetainingMap();
        FieldKey[] fieldKeys = (FieldKey[]) keyedByFieldKey.keySet().toArray(new FieldKey[keyedByFieldKey.size()]);
        Arrays.sort(fieldKeys, (Comparator) getMap().get(type));
        for(int i = 0; i < fieldKeys.length; i++) {
          result.put(fieldKeys[i], keyedByFieldKey.get(fieldKeys[i]));
        }
        ret = result;
      } else {
        ret = keyedByFieldKey;
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "sort", ret);
    }
  }

  public void registerFieldOrder(Class<?> type, String[] fields) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "registerFieldOrder", type, fields);
    try {
      getMap().put(type, new FieldComparator(fields));
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "registerFieldOrder");
    }
  }

  private static class FieldComparator implements Comparator<Object>, Serializable {

    private static final long serialVersionUID = 9038906400781460964L;

    private final String[] fieldOrder;

    public FieldComparator(String[] fields) {
      this.fieldOrder = fields;
    }

    public int compare(String first, String second) {
      if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "compare", first, second);
      int ret = 0;
      try {
        int firstPosition = -1, secondPosition = -1;
        for(int i = 0; i < this.fieldOrder.length; i++) {
          if(this.fieldOrder[i].equals(first)) {
            firstPosition = i;
          }
          if(this.fieldOrder[i].equals(second)) {
            secondPosition = i;
          }
        }
        if(firstPosition == -1) {
          ret = 100;
        } else if(secondPosition == -1) {
          ret = -100;
        } else {
          ret = firstPosition - secondPosition;
        }
        return ret;
      } finally {
        if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "compare", ret);
      }
    }

    @Override
    @SuppressWarnings({ "unused", "rawtypes" })
    public int compare(Object firstObject, Object secondObject) {
      FieldKey first = (FieldKey) firstObject, second = (FieldKey) secondObject;
      // $$$ The getDeclaringClass call causes the load of the XStream class info, needs work
      Class definedIn = first.getDeclaringClass();
      return compare(first.getFieldName(), second.getFieldName());
    }
  }
}
