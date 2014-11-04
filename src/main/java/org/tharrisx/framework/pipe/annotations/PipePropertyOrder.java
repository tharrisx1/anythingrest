package org.tharrisx.framework.pipe.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets a floating-point sort value to use in sorting the properties during marshalling.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PipePropertyOrder {

  /**
   * The class of the Codec to be used in encryption/decryption, if any.
   * @return Class<? extends Codec>
   */
  double value();
}
