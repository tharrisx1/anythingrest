package org.tharrisx.framework.pipe.codec;

/**
 * Uses a good algorithm that 'encrypts' the id using a hard-to-follow encoding.
 * 
 * This encoding is in fact reversable, but it would require knowing all of the
 * internal hard-coded values used in the algorithm, making it safe for most
 * purposes. I wouldn't use this 'encryption' in a government setting, however.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class EncryptedIdCodec implements Codec {

  @Override
  public Object encode(Object arg) {
    String ret = null;
    String numberToEncrypt = (String) arg;
    if(numberToEncrypt == null || numberToEncrypt.trim().length() == 0) {
      ret = "";
    } else {
      int numberToReturn = Integer.parseInt(numberToEncrypt);
      ret = String.valueOf((((numberToReturn & 31) << 26) + ((numberToReturn >> 5) & 0x3FFFFFF)) ^ 0xBEB0BA);
    }
    return ret;
  }

  @Override
  public Object decode(Object arg) {
    String ret = null;
    String numberToDecrypt = (String) arg;
    if(numberToDecrypt == null || numberToDecrypt.trim().length() == 0) {
      ret = "";
    } else {
      int numberToReturn = Integer.parseInt(numberToDecrypt) ^ 0xBEB0BA;
      ret = String.valueOf(((numberToReturn & 0x7C000000) >> 26) + ((numberToReturn << 5) & 0x7FFFFFFF));
    }
    return ret;
  }
}
