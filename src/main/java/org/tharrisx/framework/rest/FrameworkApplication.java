package org.tharrisx.framework.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.tharrisx.framework.rest.providers.ExceptionMappers;
import org.tharrisx.framework.rest.resources.RootResource;

/**
 * Always use this to feed the Jersey Servlet as its init-param, no one should ever need to change this, unless
 * the Jersey API changes, or Framework is extended generously.
 * 
 * @author tharrisx
 * @since 3.0.0
 * @version 3.0.0
 */
public final class FrameworkApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> ret = new HashSet<>();
    ret.add(RootResource.class);
    ret.add(ExceptionMappers.class);
    return ret;
  }
}
