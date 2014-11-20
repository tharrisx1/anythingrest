package org.tharrisx.framework.pipe.core;

import org.tharrisx.framework.pipe.PipeContextProtectionFactory;
import org.tharrisx.util.log.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.TypeConverter;

public class JsonPipeFormat extends BasePipeFormat {

  public JsonPipeFormat(PipeContextProtectionFactory pipeContextProtectionMapFactory1) {
    super(pipeContextProtectionMapFactory1);
  }

  @Override
  protected void createEngine() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "createEngine");
    try {
      Configuration jettisonConfig = new Configuration();
      jettisonConfig.setTypeConverter(new TypeConverter() {
        @Override public Object convertToJSONPrimitive(String text) {
          return text;
        }
      });
      setEngine(new XStream(createPipeContextReflectionProvider(), new JettisonMappedXmlDriver(jettisonConfig)));
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "createEngine");
    }
  }
}
