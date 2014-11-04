package org.tharrisx.framework.pipe.core;

import org.tharrisx.framework.pipe.PipeContextProtectionFactory;
import org.tharrisx.util.log.Log;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class XmlPipeFormat extends BasePipeFormat {

  public XmlPipeFormat(PipeContextProtectionFactory pipeContextProtectionMapFactory1) {
    super(pipeContextProtectionMapFactory1);
  }

  @Override
  protected void createEngine() {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "initializeEngine");
    try {
      setEngine(new XStream(createPipeContextReflectionProvider(), new StaxDriver()));
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "initializeEngine");
    }
  }
}
