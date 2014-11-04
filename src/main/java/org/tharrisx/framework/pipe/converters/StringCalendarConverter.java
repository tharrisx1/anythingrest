package org.tharrisx.framework.pipe.converters;

import java.text.ParseException;
import java.util.GregorianCalendar;

import org.tharrisx.framework.pipe.exception.PipeException;
import org.tharrisx.util.DateUtils;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.extended.GregorianCalendarConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Converts calendars to/from an example string format.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class StringCalendarConverter extends GregorianCalendarConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public boolean canConvert(Class type) {
    return type.equals(GregorianCalendar.class);
  }

  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    GregorianCalendar calendar = (GregorianCalendar) source;
    writer.setValue(DateUtils.getStringFromCalendar(calendar));
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    GregorianCalendar result = null;
    try {
      result = (GregorianCalendar) DateUtils.getCalendarFromString(reader.getValue());
    } catch(ParseException e) {
      throw new PipeException(e);
    }
    return result;
  }
}
