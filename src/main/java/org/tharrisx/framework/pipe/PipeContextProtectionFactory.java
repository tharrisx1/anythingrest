package org.tharrisx.framework.pipe;

import java.util.Map;
import java.util.Set;

/**
 * Provides a method to configure the PipeContext to ProtectionType mapping, as well as
 * the map of property encryption algorithms.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface PipeContextProtectionFactory {

  /**
   * Provide the security access level definitions
   * @return Map<PipeContext, Set<ProtectionType>>
   */
  Map<PipeContext, Set<ProtectionType>> getPipeContextProtectionMap();
}
