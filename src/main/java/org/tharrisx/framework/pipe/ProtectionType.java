package org.tharrisx.framework.pipe;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.tharrisx.framework.bean.Bean;

/**
 * Every bean property carries one of these protection types, and if they aren't explicitly defined, the
 * bean property is treated as if the PUBLIC.
 * 
 * NOTE: This is using the pre-java5 type-safe enum pattern, so that the application can define their own at runtime.
 *
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class ProtectionType {

  static class DefaultProtectionCheck implements ProtectionCheck {
    @Override
    public boolean isAuthorized(Bean bean, String userId) {
      return true;
    }
  }

  private static final Map<String, ProtectionType> MAP = new HashMap<>();

  public static final String NAME_PUBLIC = "PUBLIC";
  public static final String NAME_ADMIN_ONLY = "ADMIN_ONLY";
  public static final String NAME_HIDDEN = "HIDDEN";

  public static ProtectionType defineValue(String name) {
    return defineValue(name, null);
  }

  public static ProtectionType defineValue(String name, ProtectionCheck protectionCheck) {
    ProtectionType ret = MAP.get(name);
    if(null == ret) {
      ret = new ProtectionType(name, protectionCheck);
      MAP.put(name, ret);
    }
    return ret;
  }

  /**
   * All users can receive and transmit this bean property
   */
  public static final ProtectionType PUBLIC = defineValue(NAME_PUBLIC);

  /**
   * Only administrative calls should receive and transmit this bean property
   */
  public static final ProtectionType ADMIN_ONLY = defineValue(NAME_ADMIN_ONLY);

  /**
   * No one should ever see bean properties with this protection type marshalled
   */
  public static final ProtectionType HIDDEN = defineValue(NAME_HIDDEN);

  public static ProtectionType[] values() {
    return MAP.values().toArray(new ProtectionType[MAP.size()]);
  }

  public static ProtectionType valueOf(String name) {
    return MAP.get(name);
  }

  // properties

  private final String name;

  public String getName() {
    return this.name;
  }

  private final ProtectionCheck protectionCheck;

  public ProtectionCheck getProtectionCheck() {
    return this.protectionCheck;
  }

  private ProtectionType(String name1) {
    this(name1, new DefaultProtectionCheck());
  }

  private ProtectionType(String name1, ProtectionCheck transportProtectionCheck1) {
    this.name = name1;
    this.protectionCheck = transportProtectionCheck1;
  }

  @Override
  public boolean equals(Object object) {
    if(this == object)
      return true;
    if(!(object instanceof ProtectionType))
      return false;
    ProtectionType rhs = (ProtectionType) object;
    return new EqualsBuilder().appendSuper(super.equals(object)).append(this.getName(), rhs.getName()).isEquals();
  }

  @Override
  public int hashCode() {
    return getName().hashCode();
  }

  @Override
  public String toString() {
    return getName();
  }
}
