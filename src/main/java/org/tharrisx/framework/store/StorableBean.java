package org.tharrisx.framework.store;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.tharrisx.framework.bean.Bean;
import org.tharrisx.framework.pipe.annotations.PipePropertyOrder;

/**
 * Any Beans that wish to be used through the BeanStore API or in the REST
 * framework need to be subclasses of this class.
 * 
 * NOTE: $$$ This class currently forces a 32-character String ID field. This should be remedied.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@MappedSuperclass
// for use as base Hibernate type, the id is a max 32 character string.
public abstract class StorableBean extends Bean {

  @Id
  @Column(length = 32, nullable = false, updatable = true)
  @PipePropertyOrder(1.0)
  private String id;

  public String getId() {
    return this.id;
  }

  public void setId(String arg) {
    this.id = arg;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).appendSuper(super.toString()).append("id", getId()).toString();
  }
}
