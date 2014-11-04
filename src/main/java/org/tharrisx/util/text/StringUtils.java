package org.tharrisx.util.text;

import java.util.regex.Pattern;

/**
 * Some string manipulation methods. This class can be used by the Logging
 * framework, so this class does no logging, since would cause an endless
 * recursion.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class StringUtils {

  /**
   * Your standard global constant for a platform-dependant end-of-line
   * character string, suitable for use in platform logging or other local file
   * writing.
   */
  public static final String EOL_SYSTEM = System.getProperty("line.separator");

  /**
   * The line end character defined in the HTTP specification.
   */
  public static final String EOL_HTTP = "\r\n";

  /**
   * Pulls out all double quote characters from a string
   * 
   * @param arg String
   * @return String
   */
  public static String stripAllQuotes(String arg) {
    StringBuilder sb = new StringBuilder(arg.length());
    final int len = arg.length();
    for(int cv = 0; cv < len; cv++) {
      if('\"' != arg.charAt(cv)) {
        sb.append(arg.charAt(cv));
      }
    }
    return sb.toString();
  }

  /**
   * This strips the package from a class name.
   * 
   * @param fqcn String The fully-qualified class name
   * @return String Just the class name
   */
  public static String stripPackage(String fqcn) {
    String ret = null;
    final int spot = fqcn.lastIndexOf('.');
    ret = (-1 == spot ? "" : fqcn.substring(spot + 1, fqcn.length()));
    return ret;
  }

  /**
   * Returns true if the string is null or of zero length
   * 
   * @param arg String
   * @return boolean
   */
  public static boolean isEmpty(String arg) {
    if(arg == null || arg.length() == 0)
      return true;
    return false;
  }

  /**
   * Would this parse as an integer?
   * @param s
   * @return boolean
   */
  public static boolean isInteger(String s) {
    boolean isValid = false;
    try {
      Integer.parseInt(s);
      isValid = true;
    } catch(NumberFormatException e) {
      isValid = false;
    }
    return isValid;
  }

  /**
   * Would this parse as a double?
   * @param s
   * @return boolean
   */
  public static boolean isDouble(String s) {
    boolean isValid = false;
    try {
      Double.parseDouble(s);
      isValid = true;
    } catch(NumberFormatException e) {
      isValid = false;
    }
    return isValid;
  }

  /**
   * Validates a string is an email address via a regular expression.
   * @see org.tharrisx.util.RegexPatterns.EMAIL_PATTERN
   * @param s
   * @return boolean
   */
  public static boolean isEmail(String s) {
    boolean isEmail = false;
    if(!isEmpty(s) && Pattern.compile(RegexPatterns.EMAIL_PATTERN).matcher(s).matches()) {
      isEmail = true;
    }
    return isEmail;
  }

  /**
   * Produces debugging output for array contents
   * 
   * @param arg Object[]
   * @return String
   */
  public static String unrollArrayForDebug(Object[] arg) {
    StringBuilder retBuf = new StringBuilder();
    for(int cv = 0; cv < arg.length; cv++) {
      if(cv > 0)
        retBuf.append(", ");
      retBuf.append(arg[cv]);
    }
    return retBuf.toString();
  }

  /**
   * Returns the byte count for an HTTP response string encoded in UTF-8.
   * @param s String
   * @return int -1 if not possible to count the utf8 bytes, otherwise the correct byte count.
   */
  public static int getUTF8ByteCount(String s) {
    int count = -1;
    try {
      byte[] utf8 = s.getBytes("UTF-8");
      if(utf8 == null) {
        count = 0;
      } else {
        count = utf8.length;
      }
    } catch(Exception e) {
      // don't do anything
    }
    return count;
  }

  private StringUtils() {
    // private default constructor protects this utility class from being
    // instantiated.
  }
}
