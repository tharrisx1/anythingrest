package org.tharrisx.framework.pipe;

import java.io.Reader;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.core.PipeCore;
import org.tharrisx.framework.pipe.exception.PipeException;

import freemarker.ext.dom.NodeModel;

/**
 * Represents the user-configured protocol pipe name for bean pipe manager.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class Pipe {

  private final PipeCore core;

  public PipeCore getCore() {
    return this.core;
  }

  public Pipe(final PipeCore pipeCore1) {
    this.core = pipeCore1;
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
    return getCore().writeToString(bean, pipeContext);
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
    return getCore().writeToFreemarker(bean, pipeContext);
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
    return getCore().write(bean, pipeContext);
  }

  /**
   * Pull a bean out of a pipe based on a String.
   * 
   * @param source String The source of the character data in the specified format
   * @return Bean The unmarshalled bean
   * @throws PipeException if there is some type of failure thing
   */
  public Bean read(String source) throws PipeException {
    return getCore().read(source);
  }

  /**
   * Pull a bean out of a pipe based on a formatted character stream.
   * 
   * @param source Reader The source of the pipe
   * @return Bean The unmarshalled bean
   * @throws PipeException if there is some type of failure thing
   */
  public Bean read(Reader source) throws PipeException {
    return getCore().read(source);
  }

  @Override
  public boolean equals(final Object other) {
    if(!(other instanceof Pipe))
      return false;
    Pipe castOther = (Pipe) other;
    return new EqualsBuilder().append(getCore(), castOther.core).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(getCore()).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("core", getCore()).toString();
  }
}
