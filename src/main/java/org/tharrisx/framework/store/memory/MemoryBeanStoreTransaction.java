package org.tharrisx.framework.store.memory;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.store.BeanStoreTransaction;

/**
 * Simple test implementation.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class MemoryBeanStoreTransaction extends BeanStoreTransaction {

  private static final long serialVersionUID = 4168397445683204939L;

  protected MemoryBeanStoreTransaction(String transactionName) {
    super(transactionName);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).appendSuper(super.toString()).toString();
  }
}
