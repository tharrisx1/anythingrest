package org.tharrisx.framework.pipe;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * A super-simple set of context pipe contexts. Each property of the beans carry an access level.
 * NOTE: Using Java 1.4 compatible type-safe enum pattern
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class PipeContext {

  private static final Map<String, PipeContext> MAP = new HashMap<>();

  public static final String NAME_AS_PUBLIC = "AS_PUBLIC";
  public static final String NAME_AS_ADMIN = "AS_ADMIN";
  public static final String NAME_AS_SUPERADMIN = "AS_SUPERADMIN";
  public static final String NAME_UNMARSHAL = "UNMARSHAL";

  public static PipeContext defineValue(String name) {
    PipeContext ret = MAP.get(name);
    if(null == ret) {
      ret = new PipeContext(name);
      MAP.put(name, ret);
    }
    return ret;
  }

  /**
   * The pipeManager marshalling should skip all properties not set to ProtectionType.PUBLIC
   */
  public static final PipeContext AS_PUBLIC = defineValue(NAME_AS_PUBLIC);

  /**
   * The pipeManager marshalling should show all properties defined with ProtectionType.PUBLIC and ProtectionType.ADMIN_ONLY
   */
  public static final PipeContext AS_ADMIN = defineValue(NAME_AS_ADMIN);

  /**
   * $$$ The pipeManager marshalling should show all properties whatever, for test!
   */
  public static final PipeContext AS_SUPERADMIN = defineValue(NAME_AS_SUPERADMIN);

  /**
   * This is a pipeManager unmarshalling operation, and no property ommisions should be performed
   */
  public static final PipeContext UNMARSHAL = defineValue(NAME_UNMARSHAL);

  // enum-like automethods from Java 1.5 look-alikes

  public static PipeContext[] values() {
    return MAP.values().toArray(new PipeContext[MAP.size()]);
  }

  public static PipeContext valueOf(String name) {
    return MAP.get(name);
  }

  private final String name;

  private PipeContext(String aName) {
    this.name = aName;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public boolean equals(Object object) {
    if(!(object instanceof PipeContext))
      return false;
    PipeContext rhs = (PipeContext) object;
    return new EqualsBuilder().appendSuper(super.equals(object)).append(this.name, rhs.name).isEquals();
  }

  @Override
  public int hashCode() {
    return this.name.hashCode();
  }
}
