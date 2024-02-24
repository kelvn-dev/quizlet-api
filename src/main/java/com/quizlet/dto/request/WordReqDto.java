package com.quizlet.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class WordReqDto {
  @NotBlank private String name;
  @NotBlank private String definition;
  @NotNull private UUID topicId;
  private Boolean isMarked;
}