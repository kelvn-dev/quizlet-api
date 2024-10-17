package com.quizlet.vbmda.workcase.gen;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * GetPatientRequestV5
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPatientRequestV5 {
  @JsonProperty("pid")
  private String pid;

  @JsonProperty("name")
  private String name;

  @JsonProperty("fromDate")
  private Long fromDate;

  @JsonProperty("toDate")
  private Long toDate;

  @JsonProperty("limit")
  private Integer limit;

  @JsonProperty("offset")
  private Integer offset;

  @JsonProperty("tenantCodes")
  @Valid
  private List<String> tenantCodes = null;
}

