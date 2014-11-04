package org.tharrisx.framework.pipe.converters;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Wraps a String bean property in a CDATA SGML structure for a pipe
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class CDataConverter implements Converter {

  @SuppressWarnings("rawtypes")
  @Override
  public boolean canConvert(Class type) {
    return type.equals(String.class);
  }

  //<[CDATA[value]]>

  @Override
  public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
    writer.setValue("<[CDATA[" + value + "]]>");
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    String ret = null;
    ret = reader.getValue().trim();
    ret = ret.substring(8, ret.length() - 3);
    return ret;
  }
}
