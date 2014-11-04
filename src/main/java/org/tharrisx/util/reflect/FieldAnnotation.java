package org.tharrisx.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * One specific field's annotation, cached in the FieldAnnotationRegistry.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class FieldAnnotation<T extends Annotation> {

  private final Field field;

  public Field getField() {
    return this.field;
  }

  private final T annotation;

  public T getAnnotation() {
    return this.annotation;
  }

  FieldAnnotation(Field field1, T annotation1) {
    this.field = field1;
    this.annotation = annotation1;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("field", getField()).append("annotation", getAnnotation()).toString();
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other)
      return true;
    if(!(other instanceof FieldAnnotation))
      return false;
    @SuppressWarnings("unchecked")
    FieldAnnotation<T> castOther = (FieldAnnotation<T>) other;
    return new EqualsBuilder().append(getField(), castOther.getField()).append(getAnnotation(), castOther.getAnnotation()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(1971794009, 1967562027).append(getField()).append(getAnnotation()).toHashCode();
  }
}
