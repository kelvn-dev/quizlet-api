/** Copyright 2019 (C) VinBrain */
package com.quizlet.vbmda.common.dom;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * Base entity of all classes which support optimistic locking mechanism (contains column VERSION).
 *
 * @author Nguyen Minh Man
 */
@MappedSuperclass
public abstract class VbmdaBaseVersionedEntity extends VbmdaBaseEntity {

  private static final long serialVersionUID = 1L;

  @Version
  @Column(name = "VERSION", nullable = false)
  private int version;

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}
