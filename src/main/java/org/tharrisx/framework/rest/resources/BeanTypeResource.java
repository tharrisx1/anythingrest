package org.tharrisx.framework.rest.resources;

import java.io.IOException;
import java.io.Reader;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.bean.PageableBeanList;
import org.tharrisx.framework.rest.core.ResourceCore;
import org.tharrisx.framework.rest.core.ResourceRequestInfo;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;

/**
 * The REST resource that acts as the 'root' for this bean type, exposing list-type gets, searches, (through the pathed
 * delegate) and a POST operation to create items.
 *
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class BeanTypeResource<T extends StorableBean> {

  private static final String METHOD_FOLLOW_BEAN_TYPE_ALL_PATH = "followBeanTypeAllPath";
  private static final String METHOD_FOLLOW_BEAN_TYPE_MATCH_PATH = "followBeanTypeMatchPath";
  private static final String METHOD_FOLLOW_BEAN_INSTANCE_PATH = "followBeanInstancePath";
  private static final String METHOD_GET_PAGE_OF_BEANS = "getPageOfBeans";
  private static final String METHOD_POST_BEAN = "postBean";

  private final ResourceCore<T> resourceCore;

  protected final ResourceCore<T> getResourceCore() {
    return this.resourceCore;
  }

  private final ResourceRequestInfo resourceRequestInfo;

  protected final ResourceRequestInfo getResourceRequestInfo() {
    return this.resourceRequestInfo;
  }

  public BeanTypeResource(final ResourceCore<T> resourceCore1, final ResourceRequestInfo resourceRequestInfo1) {
    if(Log.isEnteringEnabled(BeanTypeResource.class)) Log.entering(BeanTypeResource.class, Log.METHOD_NAME_CONSTRUCTOR, resourceCore1, resourceRequestInfo1);
    this.resourceCore = resourceCore1;
    this.resourceRequestInfo = resourceRequestInfo1;
    if(Log.isExitingEnabled(BeanTypeResource.class)) Log.exiting(BeanTypeMatchResource.class, Log.METHOD_NAME_CONSTRUCTOR);
  }

  /**
   * PATH Give me the BeanTypeAllResource for this bean type.
   * Example URL: http://host:port/rest/users/all/
   * 
   * @return BeanTypeAllResource<T>
   */
  @Path("all")
  public BeanTypeAllResource<T> followBeanTypeAllPath() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_FOLLOW_BEAN_TYPE_ALL_PATH);
    BeanTypeAllResource<T> ret = null;
    try {
      ret = getResourceCore().makeBeanTypeAllResource(getResourceRequestInfo());
      return ret;
    } catch(RuntimeException e) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_FOLLOW_BEAN_TYPE_ALL_PATH, ret);
    }
  }

  /**
   * PATH Give me the BeanTypeMatchResource for this bean type.
   * Example URL: http://host:port/rest/users/match,name=value,name=value/
   * 
   * @return BeanTypeMatchResource<T>
   */
  @Path("match")
  public BeanTypeMatchResource<T> followBeanTypeMatchPath() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_FOLLOW_BEAN_TYPE_MATCH_PATH);
    BeanTypeMatchResource<T> ret = null;
    try {
      ret = getResourceCore().makeBeanTypeMatchResource(getResourceRequestInfo());
      return ret;
    } catch(RuntimeException e) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_FOLLOW_BEAN_TYPE_MATCH_PATH, ret);
    }
  }

  /**
   * PATH Give me the BeanInstanceResource for this bean's id.
   * Example URL: http://host:port/rest/users/1179264348
   * 
   * @param beanId String The id value of the bean
   * @return BeanInstanceResource<T>
   */
  @Path("{beanId: [0-9,A-F]+}")
  public BeanInstanceResource<T> followBeanInstancePath(@PathParam("beanId") final String beanId) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_FOLLOW_BEAN_INSTANCE_PATH, beanId);
    BeanInstanceResource<T> ret = null;
    try {
      ret = getResourceCore().makeBeanInstanceResource(getResourceRequestInfo(), beanId);
      return ret;
    } catch(RuntimeException e) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_FOLLOW_BEAN_INSTANCE_PATH, ret);
    }
  }

  /**
   * LEAF GET Retrieves a paged list of the Beans this resource represents.
   * Example URL: http://host:port/rest/users
   * 
   * @param start int The index into the full results the request wants the page to start at
   * @param end int The index into the full results the request wants the page to end at
   * @return Response The REST response
   */
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
  public Response getPageOfBeans(@DefaultValue("0") @QueryParam("start") final int start, @DefaultValue("9") @QueryParam("end") final int end) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_PAGE_OF_BEANS, start, end);
    Response ret = null;
    try {
      PageableBeanList<T> beans = getResourceCore().getBeanBehavior().getBeans(start, end);
      ret = getResourceCore().makeGetSuccessResponse(getResourceRequestInfo(), this, beans);
      return ret;
    } catch(RuntimeException e) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_PAGE_OF_BEANS, ret);
    }
  }

  /**
   * LEAF POST Creates the requested bean in the BeanStore.
   * 
   * Example URL: http://host:port/rest/users
   * @param entityBody Reader The body of the request, for unmarshalling
   * @return Response The REST response
   */
  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
  public Response postBean(Reader entityBody) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_POST_BEAN, entityBody);
    Response ret = null;
    try {
      Bean rawBean = getResourceCore().interpretRequestBody(getResourceRequestInfo(), entityBody);
      T bean = getResourceCore().getBeanType().cast(rawBean);
      T storedBean = getResourceCore().getBeanBehavior().postBean(bean);
      ret = getResourceCore().makePostSuccessResponse(getResourceRequestInfo(), this, storedBean);
      return ret;
    } catch(IOException e) {
      // the user dropped the connection, don't fret...
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } catch(RuntimeException e) {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_POST_BEAN, ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("resourceCore", getResourceCore()).append("resourceRequestInfo", getResourceRequestInfo()).toString();
  }
}
