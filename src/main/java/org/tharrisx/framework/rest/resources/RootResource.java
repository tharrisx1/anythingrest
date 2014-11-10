package org.tharrisx.framework.rest.resources;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.bean.BeanList;
import org.tharrisx.framework.bean.ResourceNameBean;
import org.tharrisx.framework.callstats.TimingMemento;
import org.tharrisx.framework.pipe.PipeContext;
import org.tharrisx.framework.rest.core.BeanConfiguration;
import org.tharrisx.framework.rest.core.ResourceCore;
import org.tharrisx.framework.rest.core.ResourceCoreFactory;
import org.tharrisx.framework.rest.core.ResourceRequestInfo;
import org.tharrisx.framework.rest.core.ServicesRegistry;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;

/**
 * Root REST resource, where all the BeanTypeResources 'hang' from. Main entry point to the framework application.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Singleton
@Path("/")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public final class RootResource {

  private static final String METHOD_FOLLOW_BEAN_TYPE_PATH = "followBeanTypePath";
  private static final String METHOD_GET_ROOTED_RESOURCE_NAMES = "getRootedResourceNames";
  private static final String METHOD_MAKE_RESOURCE_REQUEST_INFO = "makeResourceRequestInfo";

  @Context
  UriInfo uriInfo;

  private UriInfo getUriInfo() {
    return this.uriInfo;
  }

  @Context
  Request request;

  private Request getRequest() {
    return this.request;
  }

  @Context
  HttpHeaders httpHeaders;

  private HttpHeaders getHttpHeaders() {
    return this.httpHeaders;
  }

  @Context
  Providers providers;

  private Providers getProviders() {
    return this.providers;
  }

  /**
   * PATH Get the BeanTypeResource for the rooted type resource name, if any, or a not found response if none matches.
   * 
   * @param rootedBeanTypeResourceName String
   * @return BeanTypeResource<? extends StorableBean>
   */
  @Path("{rootedBeanTypeResourceName}")
  public BeanTypeResource<? extends StorableBean> followBeanTypePath(final @PathParam("rootedBeanTypeResourceName") String rootedBeanTypeResourceName) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_FOLLOW_BEAN_TYPE_PATH, rootedBeanTypeResourceName);
    BeanTypeResource<? extends Bean> ret = null;
    TimingMemento timing = null;
    if(ServicesRegistry.getServices().getCallStatistician().isCallStatisticsEnabled())
      timing = new TimingMemento();
    ResourceRequestInfo req = makeResourceRequestInfo(timing);
    Class<? extends StorableBean> beanType = null;
    try {
      if(null == rootedBeanTypeResourceName)
        throw new NotFoundException();
      BeanConfiguration beanConfig = ServicesRegistry.getBeanConfiguration();
      beanType = beanConfig.getRootedResourceTypeForName(rootedBeanTypeResourceName);
      ResourceCore<? extends StorableBean> resourceCore = ResourceCoreFactory.getResourceCore(beanType);
      ret = resourceCore.makeBeanTypeResource(req, beanType);
      return ret;
    } catch(RuntimeException e) {
      // $$$ temporary during debug
      Log.fatal(getClass(), METHOD_FOLLOW_BEAN_TYPE_PATH, "ERROR!", e);
      if(ServicesRegistry.getServices().getCallStatistician().isCallStatisticsEnabled()) {
        String callName = ResourceCore.makeCallName(beanType, this, "GET", req.getPipeName());
        ServicesRegistry.getServices().getCallStatistician().recordCall(callName, false, req.getTiming());
      }
      throw e;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_FOLLOW_BEAN_TYPE_PATH, ret);
    }
  }

  /**
   * LEAF GET Returns a list of the rooted resource types.
   * Example URL: http://host:port/rest/
   * 
   * @return Response
   */
  @GET
  public Response getRootedResourceNames() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_GET_ROOTED_RESOURCE_NAMES);
    Response ret = null;
    TimingMemento timing = null;
    if(ServicesRegistry.getServices().getCallStatistician().isCallStatisticsEnabled())
      timing = new TimingMemento();
    ResourceRequestInfo req = makeResourceRequestInfo(timing);
    try {
      List<ResourceNameBean> rootedResourceList = new LinkedList<>();
      for(String name : ServicesRegistry.getBeanConfiguration().getRootedResourceNames()) {
        rootedResourceList.add(new ResourceNameBean(name));
      }
      BeanList<ResourceNameBean> beans = new BeanList<>(rootedResourceList);
      String body = req.getPipe().writeToString(beans, req.getPipeContext());
      if(Log.isDebugEnabled(getClass())) Log.debug(getClass(), METHOD_GET_ROOTED_RESOURCE_NAMES, "Sending " + req.getPipeName() + " response body: " + body);
      ret = Response.ok(body).type(req.getResponseMediaType()).build();
      String callName = ResourceCore.makeCallName(ResourceNameBean.class, this, "GET", req.getPipeName());
      ServicesRegistry.getServices().getCallStatistician().recordCall(callName, true, req.getTiming());
      return ret;
    } catch(RuntimeException e) {
      // $$$ temporary during debug
      Log.fatal(getClass(), METHOD_GET_ROOTED_RESOURCE_NAMES, "ERROR!", e);
      if(ServicesRegistry.getServices().getCallStatistician().isCallStatisticsEnabled()) {
        String callName = ResourceCore.makeCallName(ResourceNameBean.class, this, "GET", req.getPipeName());
        ServicesRegistry.getServices().getCallStatistician().recordCall(callName, false, req.getTiming());
      }
      throw e;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_GET_ROOTED_RESOURCE_NAMES, ret);
    }
  }

  private ResourceRequestInfo makeResourceRequestInfo(TimingMemento timing) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), METHOD_MAKE_RESOURCE_REQUEST_INFO);
    ResourceRequestInfo ret = null;
    try {
      PipeContext pipeContext = PipeContext.AS_PUBLIC; // $$$ All calls are public, no admin rest interface for the moment.
      ret = new ResourceRequestInfo(pipeContext, getUriInfo(), getRequest(), getHttpHeaders(), getProviders(), timing);
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), METHOD_MAKE_RESOURCE_REQUEST_INFO, ret);
    }
  }
}
