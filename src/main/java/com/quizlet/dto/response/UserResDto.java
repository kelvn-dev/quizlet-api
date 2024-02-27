package com.quizlet.dto.response;

import java.util.UUID;
import lombok.Data;

@Data
public class UserResDto {
  private UUID id;
  private String name;
  private int point;
  private int studyTime;
}
