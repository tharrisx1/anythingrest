package org.tharrisx.framework.rest.resources;

import java.io.IOException;
import java.io.Reader;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.rest.core.ResourceCore;
import org.tharrisx.framework.rest.core.ResourceRequestInfo;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;

import com.sun.jersey.api.NotFoundException;

/**
 * The REST resource that acts as the item resource, providing GET, PUT, and DELETE of an existing item.
 * Example URL: http://host:port/context/users/5267AF785267AF785267AF785267AF78/
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class BeanInstanceResource<T extends StorableBean> {

  private static final String METHOD_GET_BEAN = "getBean";
  private static final String METHOD_PUT_BEAN = "putBean";
  private static final String METHOD_DELETE_BEAN = "deleteBean";

  private final ResourceCore<T> resourceCore;

  protected final ResourceCore<T> getResourceCore() {
    return this.resourceCore;
  }

  private final ResourceRequestInfo resourceRequestInfo;

  protected final ResourceRequestInfo getResourceRequestInfo() {
    return this.resourceRequestInfo;
  }

  private final String beanId;

  protected final String getBeanId() {
    return this.beanId;
  }

  public BeanInstanceResource(final ResourceCore<T> resourceCore1, final ResourceRequestInfo resourceRequestInfo1, final String beanId1) {
    if(Log.isEnteringEnabled(BeanInstanceResource.class)) Log.entering(BeanInstanceResource.class, Log.METHOD_NAME_CONSTRUCTOR, resourceCore1, resourceRequestInfo1, beanId1);
    this.resourceCore = resourceCore1;
    this.resourceRequestInfo = resourceRequestInfo1;
    this.beanId = beanId1;
    if(Log.isExitingEnabled(BeanInstanceResource.class)) Log.exiting(BeanInstanceResource.class, Log.METHOD_NAME_CONSTRUCTOR);
  }

  /**
   * Leaf GET Returns the bean for the provided id.
   * 
   * @return Response
   */
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
  public Response getBean() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_BEAN);
    Response ret = null;
    try {
      T bean = getResourceCore().getBeanBehavior().getBean(getBeanId());
      ret = getResourceCore().makeGetSuccessResponse(getResourceRequestInfo(), this, bean);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_BEAN, ret);
    }
  }

  /**
   * Leaf PUT Saves changes to the existing bean.
   * 
   * @param beanBody Reader
   * @return Response
   */
  @PUT
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
  public Response putBean(Reader beanBody) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_PUT_BEAN, beanBody);
    Response ret = null;
    try {
      try {
        getResourceCore().getBeanBehavior().putBean(getResourceCore().getBeanType().cast(getResourceCore().interpretRequestBody(getResourceRequestInfo(), beanBody)));
        ret = getResourceCore().makePutSuccessResponse(getResourceRequestInfo(), this);
      } catch(IOException e) {
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_PUT_BEAN, ret);
    }
  }

  /**
   * Leaf DELETE Deletes the existing bean.
   * 
   * @param beanBody Reader
   * @return Response
   */
  @DELETE
  public void deleteBean() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_DELETE_BEAN);
    try {
      boolean wasPresent = getResourceCore().getBeanBehavior().deleteBean(getBeanId());
      if(!wasPresent)
        throw new NotFoundException();
      getResourceCore().recordDeleteSuccess(getResourceRequestInfo(), this);
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_DELETE_BEAN);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("resourceCore", getResourceCore()).append("resourceRequestInfo", getResourceRequestInfo()).append("beanId", getBeanId()).toString();
  }
}
