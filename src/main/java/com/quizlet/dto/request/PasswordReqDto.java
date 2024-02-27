package com.quizlet.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordReqDto {
  @NotBlank private String password;
}
