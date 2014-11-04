package org.tharrisx.framework.pipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.tharrisx.util.collection.CollectionUtils;

/**
 * Default pipe context protection map factory with a map of:
 * <ul>
 *   <li>PipeContext.AS_PUBLIC - Pass along only the bean properties that have access = ProtectionType.PUBLIC
 *   <li>PipeContext.AS_ADMIN - Pass along any properties with access ProtectionType.PUBLIC or ProtectionType.ADMIN_ONLY
 *   <li>PipeContext.AS_SUPERADMIN - Pass along all defined properties that aren't omitted outright through XStream
 * </ul>
 * 
 * Custom ProtectionTypes with ProtectionCheck implementations will bypass this map, since the custom definition
 * trumps this humble algorithm.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class DefaultPipeContextProtectionFactory implements PipeContextProtectionFactory {

  protected Map<PipeContext, Set<ProtectionType>> contextProtectionMap = null;

  @Override
  public Map<PipeContext, Set<ProtectionType>> getPipeContextProtectionMap() {
    return this.contextProtectionMap;
  }

  public DefaultPipeContextProtectionFactory() {
    // initialize mutable contextProtectionMap
    this.contextProtectionMap = new HashMap<>();
    this.contextProtectionMap.put(PipeContext.AS_PUBLIC, CollectionUtils.asSet(new ProtectionType[] { ProtectionType.PUBLIC }));
    this.contextProtectionMap.put(PipeContext.AS_ADMIN, CollectionUtils.asSet(new ProtectionType[] { ProtectionType.PUBLIC, ProtectionType.ADMIN_ONLY }));
    this.contextProtectionMap.put(PipeContext.AS_SUPERADMIN, CollectionUtils.asSet(new ProtectionType[] { ProtectionType.PUBLIC, ProtectionType.ADMIN_ONLY }));
    this.contextProtectionMap.put(PipeContext.UNMARSHAL, CollectionUtils.asSet(new ProtectionType[] { ProtectionType.PUBLIC }));
  }
}
