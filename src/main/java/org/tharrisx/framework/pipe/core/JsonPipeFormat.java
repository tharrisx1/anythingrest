package org.tharrisx.framework.pipe.core;

import org.tharrisx.framework.pipe.PipeContextProtectionFactory;
import org.tharrisx.util.log.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class JsonPipeFormat extends BasePipeFormat {

  public JsonPipeFormat(PipeContextProtectionFactory pipeContextProtectionMapFactory1) {
    super(pipeContextProtectionMapFactory1);
  }

  @Override
  protected void createEngine() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "createEngine");
    try {
      setEngine(new XStream(createPipeContextReflectionProvider(), new JettisonMappedXmlDriver()));
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "createEngine");
    }
  }
}
