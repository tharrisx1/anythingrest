package org.tharrisx.framework.rest.core;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.callstats.TimingMemento;
import org.tharrisx.framework.pipe.Pipe;
import org.tharrisx.framework.pipe.PipeContext;
import org.tharrisx.util.log.Log;

public class ResourceRequestInfo {

  private final PipeContext pipeContext;

  public PipeContext getPipeContext() {
    return this.pipeContext;
  }

  private final UriInfo uriInfo;

  public UriInfo getUriInfo() {
    return this.uriInfo;
  }

  private final Request request;

  public Request getRequest() {
    return this.request;
  }

  private final HttpHeaders httpHeaders;

  public HttpHeaders getHttpHeaders() {
    return this.httpHeaders;
  }

  private final Providers providers;

  public Providers getProviders() {
    return this.providers;
  }

  private final String pipeName;

  public String getPipeName() {
    return this.pipeName;
  }

  private final Pipe pipe;

  public Pipe getPipe() {
    return this.pipe;
  }

  private final MediaType responseMediaType;

  public final MediaType getResponseMediaType() {
    return this.responseMediaType;
  }

  private final TimingMemento timing;

  public TimingMemento getTiming() {
    return this.timing;
  }

  public ResourceRequestInfo(final PipeContext pipeContext1, final UriInfo uriInfo1, final Request request1, final HttpHeaders httpHeaders1, final Providers providers1, final TimingMemento timing1) {
    if(Log.isEnteringEnabled(ResourceRequestInfo.class))
      Log.entering(ResourceRequestInfo.class, Log.METHOD_NAME_CONSTRUCTOR, pipeContext1, uriInfo1, request1, httpHeaders1, providers1);
    this.pipeContext = pipeContext1;
    this.uriInfo = uriInfo1;
    this.request = request1;
    this.httpHeaders = httpHeaders1;
    this.providers = providers1;
    MediaPipeMapper.MediaPipe mediaPipe = MediaPipeMapper.getMediaPipe(getHttpHeaders().getAcceptableMediaTypes());
    this.pipeName = mediaPipe.getPipeName();
    this.pipe = ServicesRegistry.getServices().getPipeManager().getPipe(getPipeName());
    this.responseMediaType = mediaPipe.getMediaType();
    this.timing = timing1;
    if(Log.isExitingEnabled(ResourceRequestInfo.class))
      Log.exiting(ResourceRequestInfo.class, Log.METHOD_NAME_CONSTRUCTOR);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("requestUri", getUriInfo().getRequestUri()).append("mediaType", getResponseMediaType()).toString();
  }
}
