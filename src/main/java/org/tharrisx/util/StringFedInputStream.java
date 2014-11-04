package org.tharrisx.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * In order to get around the deprecation of the Java 1.1's old
 * StringBufferInputStream, this class provides the same functionality, while
 * handling characters correctly.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class StringFedInputStream extends InputStream {

  private ByteArrayInputStream internal = null;

  public StringFedInputStream(String source) {
    this.internal = new ByteArrayInputStream(source.getBytes());
  }

  @Override
  public int read() {
    return this.internal.read();
  }
}
