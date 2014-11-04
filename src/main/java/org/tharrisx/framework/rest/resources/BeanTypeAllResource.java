package org.tharrisx.framework.rest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.BeanList;
import org.tharrisx.framework.rest.core.ResourceCore;
import org.tharrisx.framework.rest.core.ResourceRequestInfo;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;

/**
 * Generic Resource for retrieving all stored beans of a specific type.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class BeanTypeAllResource<T extends StorableBean> {

  private static final String METHOD_GET_ALL_BEANS = "getAllBeans";

  private final ResourceCore<T> resourceCore;

  public final ResourceCore<T> getResourceCore() {
    return this.resourceCore;
  }

  private final ResourceRequestInfo resourceRequestInfo;

  protected final ResourceRequestInfo getResourceRequestInfo() {
    return this.resourceRequestInfo;
  }

  public BeanTypeAllResource(final ResourceCore<T> resourceCore1, final ResourceRequestInfo resourceRequestInfo1) {
    if(Log.isEnteringEnabled(BeanTypeAllResource.class)) Log.entering(BeanTypeAllResource.class, Log.METHOD_NAME_CONSTRUCTOR, resourceCore1, resourceRequestInfo1);
    this.resourceCore = resourceCore1;
    this.resourceRequestInfo = resourceRequestInfo1;
    if(Log.isExitingEnabled(BeanTypeAllResource.class)) Log.exiting(BeanTypeAllResource.class, Log.METHOD_NAME_CONSTRUCTOR);
  }

  /**
   * LEAF GET Give me a list of all beans of the type.
   * Example URL: http://host:port/context/users/all/
   * 
   * @param propertyValuePairs String A comma delimited list of name=value pairs
   * @param start int Paging parameter
   * @param end int Paging parameter
   * @return Response
   */
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
  public Response getAllBeans() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_ALL_BEANS);
    Response ret = null;
    try {
      BeanList<T> beans = getResourceCore().getBeanBehavior().getAllBeans();
      ret = getResourceCore().makeGetSuccessResponse(getResourceRequestInfo(), this, beans);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_ALL_BEANS, ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("resourceCore", getResourceCore()).append("resourceRequestInfo", getResourceRequestInfo()).toString();
  }
}
