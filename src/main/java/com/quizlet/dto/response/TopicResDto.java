package com.quizlet.dto.response;

import java.util.UUID;
import lombok.Data;

@Data
public class TopicResDto {
  private UUID id;
  private String name;
  private boolean isPublic;
}
