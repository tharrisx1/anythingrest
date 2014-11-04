package org.tharrisx.framework.rest.resources;

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.PageableBeanList;
import org.tharrisx.framework.rest.core.ResourceCore;
import org.tharrisx.framework.rest.core.ResourceRequestInfo;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.collection.MapStringUtils;
import org.tharrisx.util.log.Log;

/**
 * Generic Resource for searching by matching the requested bean properties to the provided String values.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class BeanTypeMatchResource<T extends StorableBean> {

  private static final String METHOD_MATCH_BEANS = "matchBeans";

  private final ResourceCore<T> resourceCore;

  public final ResourceCore<T> getResourceCore() {
    return this.resourceCore;
  }

  private final ResourceRequestInfo resourceRequestInfo;

  protected final ResourceRequestInfo getResourceRequestInfo() {
    return this.resourceRequestInfo;
  }

  public BeanTypeMatchResource(final ResourceCore<T> resourceCore1, final ResourceRequestInfo resourceRequestInfo1) {
    if(Log.isEnteringEnabled(BeanTypeMatchResource.class))
      Log.entering(BeanTypeMatchResource.class, Log.METHOD_NAME_CONSTRUCTOR, resourceCore1, resourceRequestInfo1);
    this.resourceCore = resourceCore1;
    this.resourceRequestInfo = resourceRequestInfo1;
    if(Log.isExitingEnabled(BeanTypeMatchResource.class))
      Log.exiting(BeanTypeMatchResource.class, Log.METHOD_NAME_CONSTRUCTOR);
  }

  /**
   * LEAF GET Give me a page of beans matching this properties provided.
   * Example URL: http://host:port/context/users/match/gender=m,state=NY,postalCode=10018/?start=11&end=20
   * 
   * @param propertyValuePairs String A comma delimited list of name=value pairs
   * @param start int Paging parameter
   * @param end int Paging parameter
   * @return Response
   */
  @Path("{propertyValuePairs}")
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
  public Response matchBeans(@PathParam("propertyValuePairs") final String propertyValuePairs, @DefaultValue("0") @QueryParam("start") final int start,
      @DefaultValue("9") @QueryParam("end") final int end) {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), METHOD_MATCH_BEANS, propertyValuePairs, start, end);
    Response ret = null;
    try {
      Map<String, String> propertyStringMap = MapStringUtils.constructMap(Collections.singletonList(propertyValuePairs));
      PageableBeanList<T> pageOfBeans = getResourceCore().getBeanBehavior().matchBeans(start, end, propertyStringMap);
      ret = getResourceCore().makeGetSuccessResponse(getResourceRequestInfo(), this, pageOfBeans);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), METHOD_MATCH_BEANS, ret);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("resourceCore", getResourceCore()).append("resourceRequestInfo", getResourceRequestInfo()).toString();
  }
}
