package org.tharrisx.framework.pipe.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.pipe.ProtectionType;
import org.tharrisx.framework.pipe.codec.Codec;
import org.tharrisx.framework.pipe.codec.PassthroughCodec;
import org.tharrisx.util.log.Log;

/**
 * Each piped bean type registered with the PipeManager gets associated with one of these objects.
 * They hold the declared Pipe annotation information, or defaults.
 * They are constructed at startup, and then cached so we don't need to continually read the
 * annotations on each request.
 *
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
class PipeableBeanInfo {

  static class PipePropertyOrderInfo implements Comparable<PipePropertyOrderInfo> {

    private final String propertyName;

    public String getPropertyName() {
      return this.propertyName;
    }

    private final double value;

    public double getValue() {
      return this.value;
    }

    public PipePropertyOrderInfo(final String propertyName1, final double value1) {
      this.propertyName = propertyName1;
      this.value = value1;
    }

    @Override
    public String toString() {
      return getPropertyName() + "=" + getValue();
    }

    @Override
    public int hashCode() {
      return new HashCodeBuilder(-530135009, -1846462541).append(getPropertyName()).append(getValue()).toHashCode();
    }

    @Override
    public boolean equals(final Object other) {
      if(this == other)
        return true;
      if(!(other instanceof PipePropertyOrderInfo))
        return false;
      PipePropertyOrderInfo castOther = (PipePropertyOrderInfo) other;
      return new EqualsBuilder().append(getPropertyName(), castOther.getPropertyName()).append(getValue(), castOther.getValue()).isEquals();
    }

    @Override
    public int compareTo(PipePropertyOrderInfo other) {
      return Double.valueOf(getValue()).compareTo(Double.valueOf(other.getValue()));
    }
  }

  static class ProtectionInfo {

    private final String value;

    public String getValue() {
      return this.value;
    }

    public ProtectionInfo(String value1) {
      this.value = value1;
    }

    @Override
    public String toString() {
      return getValue();
    }
  }

  static class EncryptionInfo {

    private final Class<? extends Codec> value;

    public Class<? extends Codec> getValue() {
      return this.value;
    }

    public EncryptionInfo(Class<? extends Codec> value1) {
      this.value = value1;
    }

    @Override
    public String toString() {
      return getValue().getName();
    }
  }

  // cache of Bean annotation values

  SortedSet<PipePropertyOrderInfo> propertyOrderSet = new TreeSet<>();
  Map<String, ProtectionInfo> protectionMap = new HashMap<>();
  Map<String, EncryptionInfo> encryptionMap = new HashMap<>();

  PipeableBeanInfo() {
    // public default constructor
  }

  /**
   * Define property order metadata about a bean property.
   * 
   * @param propertyName String
   * @param propertyOrder PipePropertyOrderInfo
   */
  void definePropertyOrder(String propertyName, PipePropertyOrderInfo propertyOrder) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "definePropertyOrder", propertyName, propertyOrder);
    try {
      this.propertyOrderSet.add(propertyOrder);
    } finally {
      if(Log.isEnteringEnabled(getClass())) Log.exiting(getClass(), "definePropertyOrder");
    }
  }

  /**
   * Define protection metadata about a bean property.
   * 
   * @param propertyName String
   * @param protection ProtectionInfo
   */
  void defineProtection(String propertyName, ProtectionInfo protection) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "defineProtection", propertyName, protection);
    try {
      this.protectionMap.put(propertyName, protection);
    } finally {
      if(Log.isEnteringEnabled(getClass())) Log.exiting(getClass(), "defineProtection");
    }
  }

  /**
   * Explicitly define the metadata about the bean properties.
   * 
   * @param propertyName String
   * @param encryption EncryptionInfo
   */
  void defineEncryption(String propertyName, EncryptionInfo encryption) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "defineEncrypted", propertyName, encryption);
    try {
      this.encryptionMap.put(propertyName, encryption);
    } finally {
      if(Log.isEnteringEnabled(getClass())) Log.exiting(getClass(), "defineEncrypted");
    }
  }

  /**
   * Return the names of the properties in the 'pipe' order, as defined by the PropertyOrder annotation.
   * 
   * @return List<String>
   */
  List<String> getSortedPropertyNames() {
    List<String> ret = new LinkedList<>();
    for(PipePropertyOrderInfo propertyOrder : this.propertyOrderSet) {
      ret.add(propertyOrder.getPropertyName());
    }
    return ret;
  }

  /**
   * Provide the access metadata about the property with the given name.
   * 
   * @param propertyName String
   * @return ProtectionType
   */
  ProtectionType getProtectionType(String propertyName) {
    ProtectionType ret = null;
    ProtectionInfo cached = this.protectionMap.get(propertyName);
    // if there is no hit, insert a default one so we don't keep missing
    if(null == cached) {
      cached = new ProtectionInfo(ProtectionType.NAME_PUBLIC);
      defineProtection(propertyName, cached);
    }
    ret = ProtectionType.valueOf(cached.getValue());
    return ret;
  }

  /**
   * Provide the access metadata about the property with the given name.
   * 
   * @param propertyName String
   * @return Class<? extends Codec>
   */
  Class<? extends Codec> getEncryption(String propertyName) {
    Class<? extends Codec> ret = null;
    EncryptionInfo cached = this.encryptionMap.get(propertyName);
    // if there is no hit, insert a default one so we don't keep missing
    if(null == cached) {
      cached = new EncryptionInfo(PassthroughCodec.class);
      defineEncryption(propertyName, cached);
    }
    ret = cached.getValue();
    return ret;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("propertyOrderSet", this.propertyOrderSet).append("protectionMap", this.protectionMap).append("encryptionMap", this.encryptionMap).toString();
  }
}
