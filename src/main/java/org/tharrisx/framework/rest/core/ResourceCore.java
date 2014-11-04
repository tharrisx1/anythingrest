package org.tharrisx.framework.rest.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.URI;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.rest.BeanBehavior;
import org.tharrisx.framework.rest.resources.BeanInstanceResource;
import org.tharrisx.framework.rest.resources.BeanTypeAllResource;
import org.tharrisx.framework.rest.resources.BeanTypeMatchResource;
import org.tharrisx.framework.rest.resources.BeanTypeResource;
import org.tharrisx.framework.rest.resources.RootResource;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;
import org.tharrisx.util.text.StringUtils;

/**
 * Provides the glue to Resources by providing a simple internal api for handling requests.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public final class ResourceCore<T extends StorableBean> {

  private static final String METHOD_MAKE_BEAN_TYPE_RESOURCE = "makeBeanTypeResource";
  private static final String METHOD_MAKE_BEAN_TYPE_MATCH_RESOURCE = "makeBeanTypeMatchResource";
  private static final String METHOD_MAKE_BEAN_TYPE_ALL_RESOURCE = "makeBeanTypeAllResource";
  private static final String METHOD_MAKE_BEAN_INSTANCE_RESOURCE = "makeBeanInstanceResource";
  private static final String METHOD_INTERPRET_REQUEST_BODY = "interpretRequestBody";
  private static final String METHOD_GET_SUCCESS_RESPONSE = "makeGetSuccessResponse";
  private static final String METHOD_POST_SUCCESS_RESPONSE = "makePostSuccessResponse";
  private static final String METHOD_PUT_SUCCESS_RESPONSE = "makePutSuccessResponse";
  private static final String METHOD_RECORD_DELETE_SUCCESS = "recordDeleteSuccess";
  private static final String METHOD_MAKE_BEAN_BEHAVIOR = "makeBeanBehavior";

  private final Class<T> beanType;

  public final Class<T> getBeanType() {
    return this.beanType;
  }

  private final BeanBehavior<T> beanBehavior;

  public final BeanBehavior<T> getBeanBehavior() {
    return this.beanBehavior;
  }

  public ResourceCore(final Class<T> beanType1) {
    if(Log.isEnteringEnabled(ResourceCore.class))
      Log.entering(ResourceCore.class, Log.METHOD_NAME_CONSTRUCTOR, beanType1);
    this.beanType = beanType1;
    this.beanBehavior = makeBeanBehavior();
    if(Log.isExitingEnabled(ResourceCore.class))
      Log.exiting(ResourceCore.class, Log.METHOD_NAME_CONSTRUCTOR);
  }

  /**
   * Produce a BeanTypeResource instantiation for a specific bean type.
   * 
   * @param req ResourceRequestInfo
   * @return BeanTypeResource<T>
   */
  @SuppressWarnings("unchecked")
  public BeanTypeResource<T> makeBeanTypeResource(ResourceRequestInfo req, Class<? extends StorableBean> beanType1) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_MAKE_BEAN_TYPE_RESOURCE, req, beanType1);
    BeanTypeResource<T> ret = null;
    try {
      Class<? extends BeanTypeResource<? extends StorableBean>> beanTypeResourceClass = ServicesRegistry.getBeanConfiguration().getBeanTypeResourceClass(beanType1);
      Constructor<? extends BeanTypeResource<? extends StorableBean>> constructor = beanTypeResourceClass.getConstructor(ResourceCore.class, ResourceRequestInfo.class);

      ret = (BeanTypeResource<T>) constructor.newInstance(this, req);
      return ret;
    } catch(Exception e) {
      throw new WebApplicationException(e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_MAKE_BEAN_TYPE_RESOURCE, ret);
    }
  }

  /**
   * Produce a BeanTypeMatchResource instantiation for a specific bean type.
   * 
   * @param req ResourceRequestInfo
   * @return BeanTypeMatchResource<T>
   */
  @SuppressWarnings("unchecked")
  public BeanTypeMatchResource<T> makeBeanTypeMatchResource(ResourceRequestInfo req) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_MAKE_BEAN_TYPE_MATCH_RESOURCE, req);
    BeanTypeMatchResource<T> ret = null;
    try {
      Class<? extends BeanTypeMatchResource<? extends StorableBean>> beanTypeMatcherResourceClass = ServicesRegistry.getBeanConfiguration().getTypeSearchResourceClass(getBeanType());
      Constructor<? extends BeanTypeMatchResource<? extends StorableBean>> constructor = beanTypeMatcherResourceClass.getConstructor(ResourceCore.class, ResourceRequestInfo.class);
      ret = (BeanTypeMatchResource<T>) constructor.newInstance(this, req);
      return ret;
    } catch(Exception e) {
      throw new WebApplicationException(e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_MAKE_BEAN_TYPE_MATCH_RESOURCE, ret);
    }
  }

  /**
   * Produce a BeanTypeAllResource instantiation for a specific bean type.
   * 
   * @param req ResourceRequestInfo
   * @return BeanTypeAllResource<T>
   */
  @SuppressWarnings("unchecked")
  public BeanTypeAllResource<T> makeBeanTypeAllResource(ResourceRequestInfo req) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_MAKE_BEAN_TYPE_ALL_RESOURCE, req);
    BeanTypeAllResource<T> ret = null;
    try {
      Class<? extends BeanTypeAllResource<? extends StorableBean>> beanTypeMatcherResourceClass = ServicesRegistry.getBeanConfiguration().getTypeAllResourceClass(getBeanType());
      Constructor<? extends BeanTypeAllResource<? extends StorableBean>> constructor = beanTypeMatcherResourceClass.getConstructor(ResourceCore.class, ResourceRequestInfo.class);
      ret = (BeanTypeAllResource<T>) constructor.newInstance(this, req);
      return ret;
    } catch(Exception e) {
      throw new WebApplicationException(e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_MAKE_BEAN_TYPE_ALL_RESOURCE, ret);
    }
  }

  /**
   * Produce a BeanInstanceResource instantiation for a specific bean type and bean id.
   *  
   * @param req ResourceRequestInfo
   * @param beanId String
   * @return BeanInstanceResource<T>
   */
  @SuppressWarnings("unchecked")
  public BeanInstanceResource<T> makeBeanInstanceResource(ResourceRequestInfo req, String beanId) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_MAKE_BEAN_INSTANCE_RESOURCE, req, beanId);
    BeanInstanceResource<T> ret = null;
    try {
      Class<? extends BeanInstanceResource<? extends StorableBean>> beanResourceClass = ServicesRegistry.getBeanConfiguration().getInstanceResourceClass(getBeanType());
      Constructor<? extends BeanInstanceResource<? extends StorableBean>> constructor = beanResourceClass.getConstructor(ResourceCore.class, ResourceRequestInfo.class, String.class);
      ret = (BeanInstanceResource<T>) constructor.newInstance(this, req, beanId);
      return ret;
    } catch(Exception e) {
      throw new WebApplicationException(e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_MAKE_BEAN_INSTANCE_RESOURCE, ret);
    }
  }

  /**
   * Marshal a bean(s) from the body of the HTTP request.
   * 
   * @param req ResourceRequestInfo
   * @param requestBody Reader
   * @return Bean
   */
  public Bean interpretRequestBody(ResourceRequestInfo req, Reader requestBody) throws IOException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_INTERPRET_REQUEST_BODY, req, requestBody);
    Bean ret = null;
    try {
      BufferedReader reader = new BufferedReader(requestBody);
      StringBuilder bodyBuf = new StringBuilder();
      String line = reader.readLine();
      while(line != null) {
        bodyBuf.append(line);
        line = reader.readLine();
      }
      String body = bodyBuf.toString();
      if(Log.isDebugEnabled(getClass()))
        Log.debug(getClass(), METHOD_INTERPRET_REQUEST_BODY, "Interpreting " + req.getPipeName() + " request body: " + body);
      ret = req.getPipe().read(new StringReader(body));
      if(Log.isDebugEnabled(getClass()))
        Log.debug(getClass(), METHOD_INTERPRET_REQUEST_BODY, "Interpreted " + req.getPipeName() + " request body as: " + ret);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_INTERPRET_REQUEST_BODY, ret);
    }
  }

  /**
   * Return a RESTful GET success response to the caller.
   * 
   * @param req ResourceRequestInfo
   * @param bean Bean
   * @return Response
   */
  public Response makeGetSuccessResponse(ResourceRequestInfo req, Object resourceReference, Bean bean) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_GET_SUCCESS_RESPONSE, req, resourceReference, bean);
    Response ret = null;
    try {
      String body = req.getPipe().writeToString(bean, req.getPipeContext());
      if(Log.isDebugEnabled(getClass()))
        Log.debug(getClass(), METHOD_GET_SUCCESS_RESPONSE, "Sending " + req.getPipeName() + " response body: " + body);
      ret = Response.ok(body).type(req.getResponseMediaType()).build();
      String callName = makeCallName(getBeanType(), resourceReference, "GET", req.getPipeName());
      ServicesRegistry.getServices().getCallStatistician().recordCall(callName, true, req.getTiming());
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_GET_SUCCESS_RESPONSE, ret);
    }
  }

  /**
   * Return a RESTful POST success response to the caller.
   * 
   * @param req ResourceRequestInfo
   * @param resourceReference Object
   * @param bean StorableBean This must be a storable bean, because the location header in the response will contain
   *     the URI to the object, which will contain the various ids.
   * @return Response
   */
  public Response makePostSuccessResponse(ResourceRequestInfo req, Object resourceReference, StorableBean bean) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_POST_SUCCESS_RESPONSE, req, resourceReference, bean);
    Response ret = null;
    try {
      URI beanUri = UriBuilder.fromResource(resourceReference.getClass()).path(bean.getId()).build();
      if(Log.isDebugEnabled(getClass()))
        Log.debug(getClass(), METHOD_POST_SUCCESS_RESPONSE, "Sending " + req.getPipeName() + " response location: " + beanUri);
      ret = Response.created(beanUri).build();
      String callName = makeCallName(getBeanType(), resourceReference, "POST", req.getPipeName());
      ServicesRegistry.getServices().getCallStatistician().recordCall(callName, true, req.getTiming());
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_POST_SUCCESS_RESPONSE, ret);
    }
  }

  /**
   * Return a RESTful PUT success response to the caller.
   * 
   * @param req ResourceRequestInfo
   * @return Response
   */
  public Response makePutSuccessResponse(ResourceRequestInfo req, Object resourceReference) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_PUT_SUCCESS_RESPONSE, req, resourceReference);
    Response ret = null;
    try {
      ret = Response.noContent().build();
      String callName = makeCallName(getBeanType(), resourceReference, "PUT", req.getPipeName());
      ServicesRegistry.getServices().getCallStatistician().recordCall(callName, true, req.getTiming());
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_PUT_SUCCESS_RESPONSE, ret);
    }
  }

  /**
   * Record a RESTful DELETE success.
   * 
   * @param req ResourceRequestInfo
   */
  public void recordDeleteSuccess(ResourceRequestInfo req, Object resourceReference) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_RECORD_DELETE_SUCCESS, req, resourceReference);
    try {
      String callName = makeCallName(getBeanType(), resourceReference, "DELETE", req.getPipeName());
      ServicesRegistry.getServices().getCallStatistician().recordCall(callName, true, req.getTiming());
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_RECORD_DELETE_SUCCESS);
    }
  }

  public enum ResourceCategory {
    UNKNOWN, ROOT, TYPE, INSTANCE, ALL, MATCH;
  }

  public static ResourceCategory categorizeResource(Class<?> resourceReference) {
    ResourceCategory ret = ResourceCategory.UNKNOWN;
    if(RootResource.class.isAssignableFrom(resourceReference)) {
      ret = ResourceCategory.ROOT;
    } else if(BeanTypeResource.class.isAssignableFrom(resourceReference)) {
      ret = ResourceCategory.TYPE;
    } else if(BeanInstanceResource.class.isAssignableFrom(resourceReference)) {
      ret = ResourceCategory.INSTANCE;
    } else if(BeanTypeAllResource.class.isAssignableFrom(resourceReference)) {
      ret = ResourceCategory.ALL;
    } else if(BeanTypeMatchResource.class.isAssignableFrom(resourceReference)) {
      ret = ResourceCategory.MATCH;
    }
    return ret;
  }

  public static String makeCallName(Class<? extends Bean> beanType, Object resourceReference, String method, String pipeName) {
    // <Bean>-(ROOT/TYPE/INSTANCE/ALL/MATCH)-(GET/POST/PUT/DELETE)-pipe
    StringBuilder retBuf = new StringBuilder();
    retBuf.append(StringUtils.stripPackage(beanType.getName())).append('-');
    retBuf.append(categorizeResource(resourceReference.getClass())).append('-');
    retBuf.append(method).append('-');
    retBuf.append(pipeName);
    return retBuf.toString();
  }

  @SuppressWarnings("unchecked")
  private BeanBehavior<T> makeBeanBehavior() {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_MAKE_BEAN_BEHAVIOR);
    BeanBehavior<T> ret = null;
    try {
      Class<? extends BeanBehavior<? extends StorableBean>> beanBehaviorClass = ServicesRegistry.getBeanConfiguration().getBeanBehaviorClass(getBeanType());
      Constructor<? extends BeanBehavior<? extends StorableBean>> constructor = beanBehaviorClass.getConstructor(Class.class);
      ret = (BeanBehavior<T>) constructor.newInstance(getBeanType());
      return ret;
    } catch(Exception e) {
      throw new WebApplicationException(e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_MAKE_BEAN_BEHAVIOR, ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("beanType", getBeanType()).append("beanBehavior", getBeanBehavior()).toString();
  }
}
