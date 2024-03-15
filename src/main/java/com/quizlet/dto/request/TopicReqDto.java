package com.quizlet.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TopicReqDto {
  @NotBlank private String name;
  @NotBlank private String url;
  private boolean isPublic;
}
