package org.tharrisx.framework.rest.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used on PersistentBeans that defines the REST BeanTypeResource path name to use when pathing from
 * the RootResource. Only use this annotation on beans that you wish rooted on the URI. As such, it's optional.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RootedTypeResourceName {

  String value();
}
