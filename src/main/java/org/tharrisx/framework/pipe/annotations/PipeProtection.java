package org.tharrisx.framework.pipe.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.tharrisx.framework.pipe.ProtectionCheck;
import org.tharrisx.framework.pipe.ProtectionType;

/**
 * Marks a bean property to be protected during pipeManager, using a name that corresponds to one of the prebaked
 * ProtectionType names, or to the name of any application-defined TransportProtectionTypes.
 * 
 * @see ProtectionType
 * @see ProtectionCheck
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PipeProtection {

  /**
   * Set the access level of this property during pipeManager. Defaults to ProtectionType.PUBLIC, so no
   * need to set if that's what you want already.
   * NOTE: Because of the need to add new types at runtime, the type-safe enum pattern is being used.
   * This makes it impossible to use the enum values themselves in annotations. 
   * @see org.tharrisx.framework.pipe.ProtectionType
   * @return ProtectionType
   */
  String value() default (ProtectionType.NAME_PUBLIC);
}
