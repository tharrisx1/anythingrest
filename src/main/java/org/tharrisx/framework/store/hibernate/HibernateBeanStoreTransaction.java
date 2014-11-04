package org.tharrisx.framework.store.hibernate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.tharrisx.framework.store.BeanStoreTransaction;

/**
 * Extends BeanStoreTransaction to carry the elements of Hibernate required to handle a store transaction.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class HibernateBeanStoreTransaction extends BeanStoreTransaction {

  private static final long serialVersionUID = 2641399750464276715L;

  private transient Transaction transaction = null;

  Transaction getTransaction() {
    return this.transaction;
  }

  private transient Session session = null;

  Session getSession() {
    return this.session;
  }

  HibernateBeanStoreTransaction(String transactionName, Session session1, Transaction transaction1) {
    super(transactionName);
    this.session = session1;
    this.transaction = transaction1;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .appendSuper(super.toString())
        .append("session", getSession())
        .append("transaction", getTransaction())
        .toString();
  }
}
