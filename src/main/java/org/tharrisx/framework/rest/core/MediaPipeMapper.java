package org.tharrisx.framework.rest.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.util.log.Log;

/**
 * Maps HTTP media types to pipes. We've got XML and JSON formats supported, via the application/xml, application/json,
 * and text/xml media types.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
class MediaPipeMapper {

  static class MediaPipe {

    MediaType mediaType;

    public MediaType getMediaType() {
      return this.mediaType;
    }

    String pipeName;

    public String getPipeName() {
      return this.pipeName;
    }

    MediaPipe(MediaType mediaType1, String pipeName1) {
      this.mediaType = mediaType1;
      this.pipeName = pipeName1;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("mediaType", getMediaType()).append("pipeName", getPipeName()).toString();
    }
  }

  // if we want to add another representation, we've got some coding to do, since XStream doesn't
  // do any others, so, let's leave this as code for the time being. What others are there? Other
  // XML or XHTML based types? We could do those with separate wrappers on the Beans.
  private static final Map<String, String> MEDIA_PIPE_MAP = new HashMap<>();
  static {
    MEDIA_PIPE_MAP.put(MediaType.APPLICATION_XML_TYPE.toString(), "xml");
    MEDIA_PIPE_MAP.put(MediaType.APPLICATION_JSON_TYPE.toString(), "json");
    MEDIA_PIPE_MAP.put(MediaType.TEXT_XML_TYPE.toString(), "xml");
  }

  static MediaPipe getMediaPipe(List<MediaType> acceptableMediaTypes) {
    if(Log.isEnteringEnabled(MediaPipeMapper.class))
      Log.entering(MediaPipeMapper.class, "getMediaPipe", acceptableMediaTypes);
    MediaPipe ret = null;
    try {
      for(MediaType mediaType : acceptableMediaTypes) {
        if(MEDIA_PIPE_MAP.keySet().contains(mediaType.toString())) {
          ret = new MediaPipe(mediaType, MEDIA_PIPE_MAP.get(mediaType.toString()));
          break;
        }
      }
      // $$$ For debugging, if no media type matches, return xml. This allows browsers to hit it.
      //if(null == ret)
      //  throw new WebApplicationException(Response.Status.UNSUPPORTED_MEDIA_TYPE);
      if(null == ret)
        ret = new MediaPipe(MediaType.APPLICATION_XML_TYPE, "xml");
      return ret;
    } finally {
      if(Log.isExitingEnabled(MediaPipeMapper.class))
        Log.exiting(MediaPipeMapper.class, "getMediaPipe", ret);
    }
  }

  private MediaPipeMapper() {
    // private default constructor for static utility class
  }
}
