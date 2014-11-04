package org.tharrisx.framework.store.core;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.store.BeanStoreTransaction;
import org.tharrisx.framework.store.StorableBean;

/**
 * Holds information about the current store operation for error reporting.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class BeanStoreStackInfo implements Serializable {

  private static final long serialVersionUID = -5976509588418977265L;

  private final Class<? extends StorableBean> beanType;

  public Class<? extends StorableBean> getBeanType() {
    return this.beanType;
  }

  private final BeanStoreTransaction transaction;

  public BeanStoreTransaction getTransaction() {
    return this.transaction;
  }

  private final String beanStoreMethod;

  public String getBeanStoreMethod() {
    return this.beanStoreMethod;
  }

  public BeanStoreStackInfo(
      final Class<? extends StorableBean> beanType1,
      final BeanStoreTransaction transaction1,
      final String beanStoreMethod1) {
    this.beanType = beanType1;
    this.transaction = transaction1;
    this.beanStoreMethod = beanStoreMethod1;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("beanType", getBeanType())
        .append("transaction", getTransaction())
        .append("beanStoreMethod", getBeanStoreMethod())
        .toString();
  }
}
