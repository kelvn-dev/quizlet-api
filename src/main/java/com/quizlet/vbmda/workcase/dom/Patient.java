/*
 * Copyright 2019 (C) VinBrain
 */

package com.quizlet.vbmda.workcase.dom;

import com.quizlet.vbmda.common.dom.VbmdaBaseAuditableEntity;
import com.quizlet.vbmda.workcase.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.Length;

/**
 * @author Nguyen Minh Man
 */
// @Audited
@Getter
@Setter
@Entity
@Table(
    name = "patient",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"pid", "tenant_code"})})
@AttributeOverride(
    name = "id",
    column = @Column(name = "patient_id", insertable = false, updatable = false))
@GenericGenerator(
    name = "SEQ_GEN",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {@Parameter(name = "sequence_name", value = "seq_patient")})
public class Patient extends VbmdaBaseAuditableEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "pid", nullable = false)
  @NotNull @Length(min = 1, max = 50) private String pid;

  @Column(name = "first_name")
  @Length(min = 1, max = 255) private String firstName;

  @Column(name = "last_name")
  @Length(min = 1, max = 255) private String lastName;

  @Column(name = "date_of_birth")
  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private Gender gender;

  @Column(name = "location")
  @Length(max = 255) private String location;

  @Column(name = "nationality", length = 2)
  @Length(max = 2) private String nationality;

  @Column(name = "email")
  @Length(max = 255) private String email;

  @Column(name = "phone_number")
  @Length(max = 255) private String phoneNumber;

  @Transient
  public String getFirstGenderLetter() {
    return Objects.isNull(gender) ? null : gender.getFirstLetter();
  }

  @Transient
  public String getGenderSafely() {
    return Objects.nonNull(gender) ? gender.name() : null;
  }
}
