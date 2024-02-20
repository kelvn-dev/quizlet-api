package com.quizlet.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FolderReqDto {
  @NotBlank private String name;
}
