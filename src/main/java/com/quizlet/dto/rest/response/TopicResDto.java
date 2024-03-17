package com.quizlet.dto.rest.response;

import java.util.UUID;
import lombok.Data;

@Data
public class TopicResDto {
  private UUID id;
  private String name;
  private String url;
  private boolean isPublic;
}
