package org.tharrisx.util.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Constrained Map-like structure holding String to String name value pairs. The
 * data is read from separately delimited strings of names and of values. Also,
 * this class removes the need for casts all over, since when we're in Java
 * 1.4.2, we hide the casts in here for tidiness.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class NameValueMap {

  private final Map<String, String> map = new HashMap<>();

  private Map<String, String> getMap() {
    return this.map;
  }

  /**
   * Create a NameValueMap from the arguments. NOTE: If the names and values
   * strings have different numbers of items in them, the minimum items between
   * them will be utilized in the map.
   * 
   * @param names String A delimited string of the names
   * @param values String A delimited string of the values
   * @param delimiter String The delimiter to use
   */
  public NameValueMap(String names, String values, String delimiter) {
    preparePairs(names, values, delimiter);
  }

  /**
   * Lookup the value for a name
   * 
   * @param name The name to lookup
   * @return String The value for the name
   */
  public String getValue(String name) {
    return getMap().get(name);
  }

  /**
   * Returns a Set of all the names in this NameValueMap
   * 
   * @return Set<String>
   */
  public Set<String> getNames() {
    return getMap().keySet();
  }

  private void preparePairs(String argumentNames, String argumentValues, String delimiter) {
    if(null == argumentNames || null == argumentValues)
      return;
    StringTokenizer nameToker = new StringTokenizer(argumentNames, delimiter);
    StringTokenizer valueToker = new StringTokenizer(argumentValues, delimiter);
    while(nameToker.hasMoreTokens() && valueToker.hasMoreTokens()) {
      getMap().put(nameToker.nextToken(), valueToker.nextToken());
    }
  }

  @Override
  public String toString() {
    return getMap().toString();
  }
}
