package org.tharrisx.framework.pipe.codec;

/**
 * A noop codec, to use as a default. Does nothing in either direction.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class PassthroughCodec implements Codec {

  @Override
  public Object decode(Object arg) {
    return arg;
  }

  @Override
  public Object encode(Object arg) {
    return arg;
  }
}
