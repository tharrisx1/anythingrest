package org.tharrisx.framework.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Returned in a BeanList from the RootResource, each one documents a resource type exposed from the root.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@XStreamAlias("resource")
public class ResourceNameBean extends Bean {

  @XStreamAsAttribute
  private String rootName = null;

  public String getRootName() {
    return this.rootName;
  }

  public ResourceNameBean() {
    // nothing
  }

  public ResourceNameBean(String aRootName) {
    this.rootName = aRootName;
  }
}
