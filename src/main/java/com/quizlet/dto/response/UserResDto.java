package com.quizlet.dto.response;

import java.util.UUID;
import lombok.Data;

@Data
public class UserResDto {
  private UUID id;
  private String nickname;
  private String email;
}
