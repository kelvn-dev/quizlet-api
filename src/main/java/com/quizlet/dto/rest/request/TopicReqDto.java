package com.quizlet.dto.rest.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TopicReqDto {
  @NotBlank private String name;
  private String url;
  private boolean isPublic;
}
