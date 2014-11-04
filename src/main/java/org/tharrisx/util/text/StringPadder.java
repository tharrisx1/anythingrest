package org.tharrisx.util.text;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/** 
 * StringPadder provides left pad and right pad functionality for Strings
 */
public class StringPadder {

  public static String leftPad(String stringToPad, String padder, int size) {
    if(padder.length() == 0) {
      return stringToPad;
    }
    StringBuilder strb = new StringBuilder(size);
    StringCharacterIterator sci = new StringCharacterIterator(padder);
    while(strb.length() < (size - stringToPad.length())) {
      for(char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next()) {
        if(strb.length() < size - stringToPad.length()) {
          strb.insert(strb.length(), String.valueOf(ch));
        }
      }
    }
    return strb.append(stringToPad).toString();
  }

  public static String rightPad(String stringToPad, String padder, int size) {
    if(padder.length() == 0) {
      return stringToPad;
    }
    StringBuilder strb = new StringBuilder(stringToPad);
    StringCharacterIterator sci = new StringCharacterIterator(padder);
    while(strb.length() < size) {
      for(char ch = sci.first(); ch != CharacterIterator.DONE; ch = sci.next()) {
        if(strb.length() < size) {
          strb.append(String.valueOf(ch));
        }
      }
    }
    return strb.toString();
  }
}
