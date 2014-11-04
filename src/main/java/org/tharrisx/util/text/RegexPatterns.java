package org.tharrisx.util.text;

/**
 * Common regex patterns
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface RegexPatterns {

  /**
   * this is a fairly simple pattern. It checks a little bit more than just
   * looking up for the @-Character.
   */
  String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
}
