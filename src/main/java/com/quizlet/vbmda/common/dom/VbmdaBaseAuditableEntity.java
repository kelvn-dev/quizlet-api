/*
 * Copyright 2019 (C) VinBrain
 */

package com.quizlet.vbmda.common.dom;

// import net.vinbrain.vbmda.common.security.UserContextHolder;
import jakarta.persistence.*;
import java.util.Date;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Nguyen Minh Man
 */
// @FilterDef(name = VbmdaBaseAuditableEntity.TENANT_FILTER,
//        parameters = @ParamDef(name = VbmdaBaseAuditableEntity.TENANT_CODE_PROP, type = "string"))
// @Filter(name = VbmdaBaseAuditableEntity.TENANT_FILTER,
//        condition = "TENANT_CODE IN (:" + VbmdaBaseAuditableEntity.TENANT_CODE_PROP + ")")
@MappedSuperclass
public abstract class VbmdaBaseAuditableEntity extends VbmdaBaseVersionedEntity {

  private static final long serialVersionUID = 1L;

  public static final String TENANT_FILTER = "tenantCodeFilter";

  public static final String TENANT_CODE_PROP = "tenantCode";

  @Column(name = "TENANT_CODE", length = 50)
  @Length(min = 1, max = 50) private String tenantCode;

  @Column(name = "CREATED_BY", nullable = false)
  @Length(min = 1, max = 255) private String createdBy;

  @Column(name = "CREATED_AT", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "UPDATED_BY", nullable = false)
  @Length(min = 1, max = 255) private String updatedBy;

  @Column(name = "UPDATED_AT", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  public String getTenantCode() {
    return tenantCode;
  }

  public void setTenantCode(final String tenantCode) {
    this.tenantCode = tenantCode;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(final String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(final String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(final Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  /** Callback executed onSave or onFlushDirty events. */
  @PrePersist
  @PreUpdate
  public void onSave() {
    final SecurityContext securityContext = SecurityContextHolder.getContext();
    if (!isPersisted()) {
      setCreatedAt(new Date());
      setCreatedBy(securityContext.getAuthentication().getName());
      //            setTenantCode(UserContextHolder.getContext().getTenantCode());
    }

    setUpdatedAt(new Date());
    setUpdatedBy(securityContext.getAuthentication().getName());
  }
}
