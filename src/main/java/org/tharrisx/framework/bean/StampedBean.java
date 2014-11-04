package org.tharrisx.framework.bean;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.pipe.annotations.PipePropertyOrder;
import org.tharrisx.framework.pipe.converters.StringDateConverter;
import org.tharrisx.framework.store.StorableBean;

import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * Provides a common API for StorableBeans that want audit info. We could add other audit info here too.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@MappedSuperclass
public abstract class StampedBean extends StorableBean {

  @Basic
  //@Type(type = "LongTimestamp")
  @Column(length = 7, nullable = false, updatable = false)
  @PipePropertyOrder(100.0)
  @XStreamConverter(StringDateConverter.class)
  private Date created = null;

  public Date getCreated() {
    return this.created;
  }

  public void setCreated(Date arg) {
    this.created = arg;
  }

  @Basic
  //@Type(type = "LongTimestamp")
  @Column(length = 7, nullable = false, updatable = false)
  @PipePropertyOrder(100.0)
  @XStreamConverter(StringDateConverter.class)
  private Date lastChanged = null;

  public Date getLastChanged() {
    return this.lastChanged;
  }

  public void setLastChanged(Date arg) {
    this.lastChanged = arg;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).appendSuper(super.toString()).append("created", getCreated()).append("lastChanged", getLastChanged()).toString();
  }
}
