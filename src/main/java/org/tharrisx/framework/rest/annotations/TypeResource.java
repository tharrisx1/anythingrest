package org.tharrisx.framework.rest.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation required on StorableBeans to define the REST BeanTypeResource class implementation to use. 
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TypeResource {

  Class<?> value();
}
