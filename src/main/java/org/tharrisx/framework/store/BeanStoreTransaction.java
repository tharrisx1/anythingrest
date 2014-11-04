package org.tharrisx.framework.store;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Represents an abstract bean store transaction, each implementation must
 * supply a subclass that carries that implementation's required references for
 * a persisted transaction.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public abstract class BeanStoreTransaction implements Serializable {

  private static final long serialVersionUID = -8712945576078426758L;

  private final String transactionName;

  public String getTransactionName() {
    return this.transactionName;
  }

  protected BeanStoreTransaction(String aTransactionName) {
    this.transactionName = aTransactionName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("transactionName", getTransactionName()).toString();
  }
}
