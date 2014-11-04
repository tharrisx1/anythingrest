package org.tharrisx.framework.pipe.codec;

/**
 * Really generic encryption interface.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface Codec {

  /**
   * Do some encryption
   * @param arg Object
   * @return Object
   */
  Object encode(Object arg);

  /**
   * Do some decryption
   * @param arg Object
   * @return Object
   */
  Object decode(Object arg);
}
