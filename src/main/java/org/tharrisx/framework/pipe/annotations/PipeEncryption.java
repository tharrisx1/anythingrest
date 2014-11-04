package org.tharrisx.framework.pipe.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.tharrisx.framework.pipe.codec.Codec;

/**
 * Marks a Bean property as PipeEncryption, using the specified Codec class.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PipeEncryption {

  /**
   * The class of the Codec to be used in encryption/decryption, if any.
   * @return Class<? extends Codec>
   */
  Class<? extends Codec> value();
}
