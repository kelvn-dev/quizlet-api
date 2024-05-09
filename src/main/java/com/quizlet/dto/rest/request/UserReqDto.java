package com.quizlet.dto.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserReqDto {
  @NotNull private String avatar;
  @NotNull private String nickname;
  private String languageCode;
}
