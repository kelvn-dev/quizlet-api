package com.quizlet.dto.request;

import java.util.UUID;
import lombok.Data;

@Data
public class UserScoreReqDto {
  private UUID userId;
  private UUID topicId;
  private int score;
}
