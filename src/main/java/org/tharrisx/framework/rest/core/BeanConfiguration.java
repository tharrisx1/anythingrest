package org.tharrisx.framework.rest.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.rest.BeanBehavior;
import org.tharrisx.framework.rest.Services;
import org.tharrisx.framework.rest.annotations.Behavior;
import org.tharrisx.framework.rest.annotations.InstanceResource;
import org.tharrisx.framework.rest.annotations.RootedTypeResourceName;
import org.tharrisx.framework.rest.annotations.TypeAllResource;
import org.tharrisx.framework.rest.annotations.TypeMatchResource;
import org.tharrisx.framework.rest.annotations.TypeResource;
import org.tharrisx.framework.rest.resources.BeanInstanceResource;
import org.tharrisx.framework.rest.resources.BeanTypeAllResource;
import org.tharrisx.framework.rest.resources.BeanTypeMatchResource;
import org.tharrisx.framework.rest.resources.BeanTypeResource;
import org.tharrisx.framework.store.StorableBean;
import org.tharrisx.util.log.Log;
import org.tharrisx.util.reflect.ClassAnnotationRegistry;

/**
 * Reads and caches all the Bean annotation configuration settings.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class BeanConfiguration {

  static class BeanInfo {

    private final Class<? extends BeanBehavior<? extends StorableBean>> beanBehavior;

    Class<? extends BeanBehavior<? extends StorableBean>> getBeanBehavior() {
      return this.beanBehavior;
    }

    private final Class<? extends BeanTypeResource<? extends StorableBean>> beanTypeResource;

    Class<? extends BeanTypeResource<? extends StorableBean>> getBeanTypeResource() {
      return this.beanTypeResource;
    }

    private final Class<? extends BeanTypeMatchResource<? extends StorableBean>> beanTypeMatchResource;

    Class<? extends BeanTypeMatchResource<? extends StorableBean>> getBeanTypeSearchResource() {
      return this.beanTypeMatchResource;
    }

    private final Class<? extends BeanTypeAllResource<? extends StorableBean>> beanTypeAllResource;

    Class<? extends BeanTypeAllResource<? extends StorableBean>> getBeanTypeAllResource() {
      return this.beanTypeAllResource;
    }

    private final Class<? extends BeanInstanceResource<? extends StorableBean>> beanInstanceResource;

    Class<? extends BeanInstanceResource<? extends StorableBean>> getBeanInstanceResource() {
      return this.beanInstanceResource;
    }

    public BeanInfo(final Class<? extends BeanBehavior<? extends StorableBean>> beanBehavior1, final Class<? extends BeanTypeResource<? extends StorableBean>> beanTypeResource1,
        final Class<? extends BeanTypeMatchResource<? extends StorableBean>> beanTypeMatcherResource1, final Class<? extends BeanTypeAllResource<? extends StorableBean>> beanTypeAllResource1,
        final Class<? extends BeanInstanceResource<? extends StorableBean>> beanResource1) {
      this.beanBehavior = beanBehavior1;
      this.beanTypeResource = beanTypeResource1;
      this.beanTypeMatchResource = beanTypeMatcherResource1;
      this.beanTypeAllResource = beanTypeAllResource1;
      this.beanInstanceResource = beanResource1;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this).append("beanBehavior", getBeanBehavior()).append("beanTypeResource", getBeanTypeResource()).append("beanTypeMatchResource", getBeanTypeSearchResource())
          .append("beanTypeAllResource", getBeanTypeAllResource()).append("beanInstanceResource", getBeanInstanceResource()).toString();
    }
  }

  private final Map<String, Class<? extends StorableBean>> rootedResourceNameTypeMap = new HashMap<>();

  private Map<String, Class<? extends StorableBean>> getRootedResourceNameTypeMap() {
    return this.rootedResourceNameTypeMap;
  }

  private final Map<Class<? extends StorableBean>, BeanInfo> resourceTypeMappingsMap = new HashMap<>();

  private Map<Class<? extends StorableBean>, BeanInfo> getResourceTypeMappingsMap() {
    return this.resourceTypeMappingsMap;
  }

  public BeanConfiguration(final Services services) {
    initializeBeanMetadata(services);
  }

  public Set<String> getRootedResourceNames() {
    return getRootedResourceNameTypeMap().keySet();
  }

  public Class<? extends StorableBean> getRootedResourceTypeForName(String rootedResourceTypeName) {
    Class<? extends StorableBean> ret = null;
    ret = getRootedResourceNameTypeMap().get(rootedResourceTypeName);
    return ret;
  }

  public Class<? extends BeanBehavior<? extends StorableBean>> getBeanBehaviorClass(Class<? extends StorableBean> beanType) {
    Class<? extends BeanBehavior<? extends StorableBean>> ret = null;
    ret = getResourceTypeMappingsMap().get(beanType).getBeanBehavior();
    return ret;
  }

  public Class<? extends BeanTypeResource<? extends StorableBean>> getBeanTypeResourceClass(Class<? extends StorableBean> beanType) {
    Class<? extends BeanTypeResource<? extends StorableBean>> ret = null;
    ret = getResourceTypeMappingsMap().get(beanType).getBeanTypeResource();
    return ret;
  }

  public Class<? extends BeanTypeMatchResource<? extends StorableBean>> getTypeSearchResourceClass(Class<? extends StorableBean> beanType) {
    Class<? extends BeanTypeMatchResource<? extends StorableBean>> ret = null;
    ret = getResourceTypeMappingsMap().get(beanType).getBeanTypeSearchResource();
    return ret;
  }

  public Class<? extends BeanTypeAllResource<? extends StorableBean>> getTypeAllResourceClass(Class<? extends StorableBean> beanType) {
    Class<? extends BeanTypeAllResource<? extends StorableBean>> ret = null;
    ret = getResourceTypeMappingsMap().get(beanType).getBeanTypeAllResource();
    return ret;
  }

  public Class<? extends BeanInstanceResource<? extends StorableBean>> getInstanceResourceClass(Class<? extends StorableBean> beanType) {
    Class<? extends BeanInstanceResource<? extends StorableBean>> ret = null;
    ret = getResourceTypeMappingsMap().get(beanType).getBeanInstanceResource();
    return ret;
  }

  @SuppressWarnings({ "unchecked" })
  // jumping over default class mappings and generics trickery
  private void initializeBeanMetadata(Services services) {
    if(Log.isEnteringEnabled(getClass())) Log.entering(getClass(), "initializeBeanMetadata", services);
    // read and interpret the framework annotations from the beans
    try {
      RootedTypeResourceName rootedTypeResourceName;
      Behavior behavior;
      TypeResource typeResource;
      TypeMatchResource typeMatchResource;
      TypeAllResource typeAllResource;
      InstanceResource instanceResource;
      for(Class<? extends StorableBean> beanType : services.getStorableBeanTypes()) {
        rootedTypeResourceName = ClassAnnotationRegistry.get().getClassAnnotation(beanType, RootedTypeResourceName.class);
        if(null != rootedTypeResourceName) {
          getRootedResourceNameTypeMap().put(rootedTypeResourceName.value(), beanType);
        }
        behavior = ClassAnnotationRegistry.get().getClassAnnotation(beanType, Behavior.class);
        typeResource = ClassAnnotationRegistry.get().getClassAnnotation(beanType, TypeResource.class);
        typeMatchResource = ClassAnnotationRegistry.get().getClassAnnotation(beanType, TypeMatchResource.class);
        typeAllResource = ClassAnnotationRegistry.get().getClassAnnotation(beanType, TypeAllResource.class);
        instanceResource = ClassAnnotationRegistry.get().getClassAnnotation(beanType, InstanceResource.class);
        Class<? extends BeanBehavior<? extends StorableBean>> beanBehavior = (Class<? extends BeanBehavior<? extends StorableBean>>) (null == behavior ? BeanBehavior.class : behavior.value());
        Class<? extends BeanTypeResource<? extends StorableBean>> beanTypeResource = (Class<? extends BeanTypeResource<? extends StorableBean>>) (null == typeResource ? BeanTypeResource.class : typeResource.value());
        Class<? extends BeanTypeMatchResource<? extends StorableBean>> beanTypeSearchResource = (Class<? extends BeanTypeMatchResource<? extends StorableBean>>) (null == typeMatchResource ? BeanTypeMatchResource.class : typeMatchResource.value());
        Class<? extends BeanTypeAllResource<? extends StorableBean>> beanTypeAllResource = (Class<? extends BeanTypeAllResource<? extends StorableBean>>) (null == typeAllResource ? BeanTypeAllResource.class : typeAllResource.value());
        Class<? extends BeanInstanceResource<? extends StorableBean>> beanInstanceResource = (Class<? extends BeanInstanceResource<? extends StorableBean>>) (null == instanceResource ? BeanInstanceResource.class : instanceResource.value());
        getResourceTypeMappingsMap().put(beanType, new BeanInfo(beanBehavior, beanTypeResource, beanTypeSearchResource, beanTypeAllResource, beanInstanceResource));
        if(Log.isDebugEnabled(getClass())) {
          BeanInfo info = getResourceTypeMappingsMap().get(beanType);
          Log.debug(getClass(), "initializeBeanMetadata", "BeanInfo loaded for " + beanType);
          if(null != rootedTypeResourceName) {
            Log.debug(getClass(), "initializeBeanMetadata", "  rootedTypeResourceName=" + rootedTypeResourceName.value());
          }
          Log.debug(getClass(), "initializeBeanMetadata", "  beanBehavior=" + info.getBeanBehavior());
          Log.debug(getClass(), "initializeBeanMetadata", "  beanTypeResource=" + info.getBeanTypeResource());
          Log.debug(getClass(), "initializeBeanMetadata", "  beanTypeMatchResource=" + info.getBeanTypeSearchResource());
          Log.debug(getClass(), "initializeBeanMetadata", "  beanInstanceResource=" + info.getBeanInstanceResource());
        }
      }
    } finally {
      if(Log.isExitingEnabled(getClass())) Log.exiting(getClass(), "initializeBeanMetadata");
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("rootedResourceNameTypeMap", getRootedResourceNameTypeMap())
        .append("resourceTypeMappingsMap", getResourceTypeMappingsMap())
        .toString();
  }
}
