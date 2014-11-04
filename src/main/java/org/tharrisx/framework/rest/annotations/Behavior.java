package org.tharrisx.framework.rest.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.tharrisx.framework.rest.BeanBehavior;
import org.tharrisx.framework.store.StorableBean;

/**
 * Annotation used on StorableBeans to define the REST BeanBehavior class implementation to use. 
 * If omitted, the generic default will be used.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Behavior {

  Class<BeanBehavior<? extends StorableBean>> value();
}
