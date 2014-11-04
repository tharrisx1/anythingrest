package org.tharrisx.framework.pipe.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.PipeContext;
import org.tharrisx.framework.pipe.PipeFormat;
import org.tharrisx.framework.pipe.exception.PipeException;
import org.tharrisx.util.log.Log;
import org.tharrisx.util.text.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import freemarker.ext.dom.NodeModel;

/**
 * The underlying implementation of the pipe manager
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class PipeCore {

  private String pipeName = null;

  String getPipeName() {
    return this.pipeName;
  }

  private PipeFormat pipeFormat = null;

  PipeFormat getPipeFormat() {
    return this.pipeFormat;
  }

  PipeCore(final String pipeName1, final PipeFormat pipeFormat1) {
    this.pipeName = pipeName1;
    this.pipeFormat = pipeFormat1;
  }

  /**
   * Put the bean into the pipe and out into a String.
   * 
   * @param bean Bean The bean to marshal
   * @param pipeContext PipeContext The access level to act as
   * @return String A String containing the marshalled bean
   * @throws PipeException if there is some type of failure thing
   */
  public String writeToString(Bean bean, PipeContext pipeContext) throws PipeException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "writeToString", bean, pipeContext);
    String ret = null;
    try {
      ret = convertReaderToString(write(bean, pipeContext));
      return ret;
    } catch(IOException e) {
      throw new PipeException("Error putting the bean " + bean + " to a String.", e);
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "writeToString", ret);
    }
  }

  /**
   * Put the bean into a pipe and out into a Freemarker NodeModel object, ready to be dropped into a page.
   * 
   * @param bean Bean The bean to put
   * @param pipeContext PipeContext The access level to act as
   * @return NodeModel A Freemarker NodeModel object, that can be referred to in a Freemarker page template
   * @throws PipeException if there is some type of failure thing
   */
  public NodeModel writeToFreemarker(Bean bean, PipeContext pipeContext) throws PipeException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "writeToFreemarker", bean, pipeContext);
    NodeModel ret = null;
    Reader source = null;
    try {
      source = write(bean, pipeContext);
      ret = NodeModel.parse(new InputSource(source), true, true);
      return ret;
    } catch(SAXException e) {
      throw new PipeException("Error marshalling to freemarker the bean: " + bean, e);
    } catch(IOException e) {
      throw new PipeException("Error marshalling to freemarker the bean: " + bean, e);
    } catch(ParserConfigurationException e) {
      throw new PipeException("Error marshalling to freemarker the bean: " + bean, e);
    } finally {
      if(null != source) {
        try {
          source.close();
        } catch(IOException e) {
          throw new PipeException("Error closing the reader stream while marshalling to freemarker the bean " + bean, e);
        }
      }
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "writeToFreemarker", ret);
    }
  }

  /**
   * Put the bean into a pipe and out via a character stream.
   * NOTE: Make sure to close the Reader when done!
   * 
   * @param bean Bean The bean to marshal
   * @param pipeContext PipeContext The access level to act as
   * @return Reader A stream of character data containing the marshalled bean 
   * @throws PipeException if there is some type of failure thing
   */
  public Reader write(Bean bean, PipeContext pipeContext) throws PipeException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "write", bean, pipeContext);
    Reader ret = null;
    try {
      StringWriter writer = new StringWriter();
      ((BasePipeFormat) getPipeFormat()).write(bean, writer, pipeContext);
      ret = new StringReader(writer.toString());
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "write", ret);
    }
  }

  /**
   * Pull a bean out of a pipe based on a String.
   * 
   * @param source String The source of the character data in the specified format
   * @return Bean The unmarshalled bean
   * @throws PipeException if there is some type of failure thing
   */
  public Bean read(String source) throws PipeException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "read", source);
    Bean ret = null;
    try {
      ret = read(new StringReader(source));
      return ret;
    } finally {
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "read", ret);
    }
  }

  /**
   * Pull a bean out of a pipe based on a formatted character stream.
   * 
   * @param source Reader The source of the pipe
   * @return Bean The unmarshalled bean
   * @throws PipeException if there is some type of failure thing
   */
  public Bean read(Reader source) throws PipeException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "read", source);
    Bean ret = null;
    try {
      ret = ((BasePipeFormat) getPipeFormat()).read(source);
      return ret;
    } finally {
      if(null != source)
        try {
          source.close();
        } catch(IOException e) {
          throw new PipeException("Error closing reader stream while unmarshalling " + source, e);
        }
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "read", ret);
    }
  }

  private String convertReaderToString(Reader source) throws IOException {
    if(Log.isEnteringEnabled(getClass()))
      Log.entering(getClass(), "convertStreamToString", source);
    String ret = null;
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(source);
      StringBuilder buf = new StringBuilder();
      String line = null;
      do {
        line = reader.readLine();
        if(line != null)
          buf.append(line).append(StringUtils.EOL_HTTP);
      } while(line != null);
      ret = buf.toString().trim();
      return ret;
    } finally {
      if(null != reader)
        reader.close();
      if(null != source)
        source.close();
      if(Log.isExitingEnabled(getClass()))
        Log.exiting(getClass(), "convertStreamToString", ret);
    }
  }

  @Override
  public boolean equals(final Object other) {
    if(!(other instanceof PipeCore))
      return false;
    PipeCore castOther = (PipeCore) other;
    return new EqualsBuilder().append(getPipeName(), castOther.getPipeName()).append(getPipeFormat(), castOther.getPipeFormat()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getPipeName()).append(getPipeFormat()).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("pipeName", getPipeName()).append("pipeFormat", getPipeFormat()).toString();
  }
}
