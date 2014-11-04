package org.tharrisx.util.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Some collection conversion methods that have no other home but here. Use
 * wisely.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class CollectionUtils {

  public static <T> Set<T> asSet(T[] arg) {
    return new HashSet<>(Arrays.asList(arg));
  }

  public static <T> List<T> asList(T[] arg) {
    return new ArrayList<>(Arrays.asList(arg));
  }

  private CollectionUtils() {
    // private constructor protects this from being instantiated.
  }
}
