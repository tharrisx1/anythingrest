package org.tharrisx.util.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Converts a list of strings, which when concatenated are of the form:
 * name1=value1,name2=value2,etc... to a Map of Strings to Strings where the
 * keys are the names and the values are the values.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class MapStringUtils {

  public static Map<String, String> constructMap(List<String> listOfStrings) {
    Map<String, String> ret = new HashMap<>();
    StringBuilder buf = new StringBuilder();
    for(int cv = 0; cv < listOfStrings.size(); cv++) {
      if(null != listOfStrings.get(cv)) {
        buf.append(listOfStrings.get(cv));
      }
    }
    String completeString = buf.toString();
    if(!"".equals(completeString) && null != completeString && !"null".equals(completeString)) {
      StringTokenizer completeToker = new StringTokenizer(completeString, ",");
      while(completeToker.hasMoreTokens()) {
        StringTokenizer pairToker = new StringTokenizer(completeToker.nextToken(), "=");
        ret.put(pairToker.nextToken(), pairToker.nextToken());
      }
    }
    return ret;
  }

  public static List<String> constructList(Map<String, String> nameValueMap, int maxCharsPerString) {
    List<String> ret = null;
    Iterator<Map.Entry<String, String>> entryItr = nameValueMap.entrySet().iterator();
    StringBuilder completeStringBuf = new StringBuilder();
    boolean onFirst = true;
    Map.Entry<String, String> entry = null;
    while(entryItr.hasNext()) {
      entry = entryItr.next();
      if(!onFirst) {
        completeStringBuf.append(",");
      } else {
        onFirst = false;
      }
      completeStringBuf.append(entry.getKey()).append("=").append(entry.getValue());
    }
    String completeString = completeStringBuf.toString();
    final int pageCount = (completeString.length() / maxCharsPerString) + 1;
    ret = new ArrayList<>(pageCount);
    int spot = 0;
    for(int cv = 0; cv < pageCount - 1; cv++) {
      ret.add(completeString.substring(spot, spot + maxCharsPerString));
      spot += maxCharsPerString;
    }
    if(spot < completeString.length())
      ret.add(completeString.substring(spot));
    return ret;
  }
}
