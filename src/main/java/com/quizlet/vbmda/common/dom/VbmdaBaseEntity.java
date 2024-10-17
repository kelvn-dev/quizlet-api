/** Copyright 2019 (C) VinBrain */
package com.quizlet.vbmda.common.dom;

import jakarta.persistence.*;
import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Base class for all VBMDa Backend entities. <br>
 *
 * @author Nguyen Minh Man
 */
@MappedSuperclass
public abstract class VbmdaBaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  /** A special entity ID for transient one (i.e. this entity hasn't been persisted). */
  public static final long TRANSIENT_ENTITY_ID = -1;

  /**
   * Has the hashCode value been leaked while being in transient state? e.g. hashcode is asking
   * twice on the same object: one when the object is being in transient state (not saved) and one
   * when it is saved.
   */
  @Transient private boolean transientHashCodeLeaked = false;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GEN")
  protected Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Check if this entity has not been persisted yet.
   *
   * @return true or false
   */
  @Transient
  public boolean isPersisted() {
    return this.id != null && this.id > TRANSIENT_ENTITY_ID;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }

    //        final Class<?> thisClass =
    // HibernateProxyHelper.getClassWithoutInitializingProxy(this);
    //        final Class<?> otherClass =
    // HibernateProxyHelper.getClassWithoutInitializingProxy(obj);
    //        if (thisClass != otherClass) {
    //            return false;
    //        }

    if (obj instanceof VbmdaBaseEntity) {
      final VbmdaBaseEntity other = (VbmdaBaseEntity) obj;
      if (isPersisted() && other.isPersisted()) {
        return new EqualsBuilder().append(id, other.id).isEquals();
      }
      // if one of entity is new (transient), they are considered not equal.
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (!isPersisted()) { // is new or is in transient state.
      transientHashCodeLeaked = true;
      // @sonarqube:off
      return -super.hashCode(); // NOSONAR
      // @sonarqube:on
    }

    // because hashcode has just been asked for when the object is in transient state at that time,
    // super.hashCode(); is returned. Now for consistency, we return the same value.
    if (transientHashCodeLeaked) {
      // @sonarqube:off
      return -super.hashCode(); // NOSONAR
      // @sonarqube:on
    }
    return new HashCodeBuilder().append(id).toHashCode();
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + id + ")";
  }
}
