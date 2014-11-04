package org.tharrisx.framework.pipe.converters;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.tharrisx.framework.pipe.exception.PipeException;
import org.tharrisx.util.DateUtils;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.extended.GregorianCalendarConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Converts dates to/from an example string format.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class StringDateConverter extends GregorianCalendarConverter {

  @SuppressWarnings("rawtypes")
  @Override
  public boolean canConvert(Class type) {
    return type.equals(Date.class);
  }

  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    Date date = (Date) source;
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    writer.setValue(DateUtils.getStringFromCalendar(calendar));
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    Date result = null;
    try {
      result = ((GregorianCalendar) DateUtils.getCalendarFromString(reader.getValue())).getTime();
    } catch(ParseException e) {
      throw new PipeException(e);
    }
    return result;
  }
}
