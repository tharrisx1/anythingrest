package org.tharrisx.framework.bean;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.pipe.ProtectionType;
import org.tharrisx.framework.pipe.annotations.PipePropertyOrder;
import org.tharrisx.framework.pipe.annotations.PipeProtection;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * A transportable carrier for a homogeneous list of beans.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@XStreamAlias("items")
public final class BeanList<T extends Bean> extends Bean {

  /**
   * The beans this list carries are in this nested list.
   */
  @XStreamImplicit
  @PipePropertyOrder(3.0)
  private LinkedList<T> items;

  public LinkedList<T> getItems() {
    return this.items;
  }

  /**
   * $$$ For future use in conditional GETs on lists for cache control
   */
  @PipeProtection(ProtectionType.NAME_HIDDEN)
  private Date lastModifed;

  public Date getLastModifed() {
    return this.lastModifed;
  }

  /**
   * $$$ For future use in conditional GETs on lists for cache control
   */
  @PipeProtection(ProtectionType.NAME_HIDDEN)
  private String tag;

  public String getTag() {
    return this.tag;
  }

  public BeanList() {
    this(new LinkedList<T>(), new Date(), "NIL");
  }

  public BeanList(List<T> items1) {
    this(items1, null, null);
  }

  public BeanList(List<T> items1, Date lastModifed1, String tag1) {
    this.items = new LinkedList<>(items1);
    this.lastModifed = lastModifed1;
    this.tag = tag1;
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other)
      return true;
    if(!(other instanceof BeanList))
      return false;
    BeanList<?> castOther = (BeanList<?>) other;
    return new EqualsBuilder()
        .append(getItems(), castOther.getItems())
        .append(getLastModifed(), castOther.getLastModifed())
        .append(getTag(), castOther.getTag())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(-530135009, -1846462541)
        .append(getItems())
        .append(getLastModifed())
        .append(getTag())
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .appendSuper(super.toString())
        .append("items", getItems())
        .append("lastModifed", getLastModifed())
        .append("tag", getTag())
        .toString();
  }
}
