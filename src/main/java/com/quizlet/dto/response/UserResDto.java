package com.quizlet.dto.response;

import lombok.Data;

@Data
public class UserResDto {
  private String id;
  private String nickname;
  private String email;
  private String avatar;
}
