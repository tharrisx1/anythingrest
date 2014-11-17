package org.tharrisx.framework.bean;

import java.util.Collections;
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
 * A transportable carrier for a paged subset of an underlying homogeneous list of beans.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@XStreamAlias("pageOfItems")
public class PageableBeanList<T extends Bean> extends Bean {

  /**
   * The total count of beans in the underlying list
   */
  @PipePropertyOrder(1.0)
  private int totalCount;

  public int getTotalCount() {
    return this.totalCount;
  }

  /**
   * The start index of this page
   */
  @PipePropertyOrder(2.0)
  private int start;

  public int getStart() {
    return this.start;
  }

  /**
   * The end index of this page
   */
  @PipePropertyOrder(3.0)
  private int end;

  public int getEnd() {
    return this.end;
  }

  /**
   * The sortBy comma-separated list of field names for the query
   */
  @PipePropertyOrder(2.0)
  private String sortBy;

  public String getSortBy() {
    return this.sortBy;
  }

  /**
   * The sortDirection comma-separated list of sort directions for the query
   */
  @PipePropertyOrder(3.0)
  private String sortDirection;

  public String getSortDirection() {
    return this.sortDirection;
  }

  /**
   * The beans this list carries are in this nested list.
   */
  @XStreamImplicit
  @PipePropertyOrder(4.0)
  private LinkedList<T> items;

  public LinkedList<T> getItems() {
    return this.items;
  }

  /**
   * For future use in conditional GETs on lists for cache control
   */
  @PipeProtection(ProtectionType.NAME_HIDDEN)
  private Date lastModifed;

  public Date getLastModifed() {
    return this.lastModifed;
  }

  /**
   * For future use in conditional GETs on lists for cache control
   */
  @PipeProtection(ProtectionType.NAME_HIDDEN)
  private String tag;

  public String getTag() {
    return this.tag;
  }

  public PageableBeanList() {
    this(Collections.<T> emptyList(), 0, 0, 0, "", "");
  }

  public PageableBeanList(List<T> items1, int totalCount1, int start1, int end1, String sortBy1, String sortDirection1) {
    this(items1, totalCount1, start1, end1, sortBy1, sortDirection1, new Date(), "");
  }

  public PageableBeanList(List<T> items1, int totalCount1, int start1, int end1, String sortBy1, String sortDirection1, Date lastModifed1, String tag1) {
    this.totalCount = totalCount1;
    this.start = start1;
    this.end = end1;
    this.sortBy = sortBy1;
    this.sortDirection = sortDirection1;
    this.items = new LinkedList<>(items1);
    this.lastModifed = lastModifed1;
    this.tag = tag1;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(final Object other) {
    if(this == other)
      return true;
    if(!(other instanceof PageableBeanList))
      return false;
    PageableBeanList<? extends Bean> castOther = (PageableBeanList<? extends Bean>) other;
    return new EqualsBuilder()
        .appendSuper(super.equals(castOther))
        .append(getTotalCount(), castOther.getTotalCount())
        .append(getItems(), castOther.getItems())
        .append(getLastModifed(), castOther.getLastModifed())
        .append(getTag(), castOther.getTag())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(-530135009, -1846462541)
        .appendSuper(super.hashCode())
        .append(getTotalCount())
        .append(getItems())
        .append(getLastModifed())
        .append(getTag())
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .appendSuper(super.toString())
        .append("totalCount", getTotalCount())
        .append("items", getItems())
        .append("lastModifed", getLastModifed())
        .append("tag", getTag())
        .toString();
  }
}
