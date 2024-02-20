package com.kelvn.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass // ensure that won't have a separate representation as table
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid2")
  @Column(
      name = "id",
      columnDefinition = "BINARY(16)", // make the value of UUID more readable
      updatable = false,
      nullable = false)
  private UUID id;

  //  @CreationTimestamp
  //  @Column(name = "created_at", updatable = false)
  //  private LocalDateTime createdAt;

  //  @UpdateTimestamp
  //  @Column(name = "updated_at")
  //  private LocalDateTime updatedAt;

  @Column(name = "created_at", updatable = false)
  private long createdAt;

  @Column(name = "updated_at")
  private long updatedAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @LastModifiedBy
  @Column(name = "updated_by")
  private String updatedBy;

  // @Column(name = "is_deleted")
  // private boolean isDeleted;

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof BaseModel)) return false;
    BaseModel that = (BaseModel) object;
    return this.id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now().getEpochSecond();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now().getEpochSecond();
  }
}
