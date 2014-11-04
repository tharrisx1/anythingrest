package org.tharrisx.util.collection;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Convert from String to List and List to String, via a delimiter.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class ListStringUtils {

  public static List<String> getList(String arg, String delimiter) {
    List<String> ret = new LinkedList<>();
    StringTokenizer toker = new StringTokenizer(arg, delimiter);
    while(toker.hasMoreTokens())
      ret.add(toker.nextToken());
    return ret;
  }

  public static String getString(List<String> arg, String delimiter) {
    StringBuilder retBuf = new StringBuilder();
    boolean onFirst = true;
    for(String item : arg) {
      if(onFirst)
        onFirst = false;
      else
        retBuf.append(delimiter);
      retBuf.append(item);
    }
    return retBuf.toString();
  }
}
