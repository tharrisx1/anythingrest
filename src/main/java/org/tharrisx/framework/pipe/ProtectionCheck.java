package org.tharrisx.framework.pipe;

import org.tharrisx.framework.bean.Bean;

/**
 * Defines an application callback API that allows an application to define custom pipeManager protection checks
 * on bean properties.
 * 
 * To use:
 * <ol>
 *   <li>Add your own custom ProtectionType at startup, in a static initializer of your bean class:
 * <pre>
 * ProtectionType.defineValue("myCustomProtectionType", new ProtectionCheck() {
 *     public boolean isAuthorized(Bean bean, String userId) {
 *         // perform check...
 *         return true or false;
 *     }
 * });
 * </pre>
 *   <li>Then, utilize your new ProtectionType on your Bean properties:
 * <pre>
 * @ProtectionType("myCustomProtectionType")
 * String protectedProperty;
 * </pre>
 * </ol>
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public interface ProtectionCheck {

  /**
   * Return true if and only if the specified user should be able to receive and transmit bean properties
   * annotated with PipeProtection.
   * @param bean The bean currently being transported
   * @param userId The id of the 'user' involved in the bean transportation operation
   * @param protectionType The type-safe enum value this check is associated with
   * @return boolean
   */
  boolean isAuthorized(Bean bean, String userId);
}
