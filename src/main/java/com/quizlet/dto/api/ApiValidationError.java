package com.quizlet.dto.api;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
  private String field;
  private Object rejectedValue;
  private String message;

  ApiValidationError(String message) {
    this.message = message;
  }
}
