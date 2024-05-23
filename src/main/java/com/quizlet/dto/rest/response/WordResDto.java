package com.quizlet.dto.rest.response;

import java.util.UUID;
import lombok.Data;

@Data
public class WordResDto {
  private UUID id;
  private String name;
  private String definition;
  private UUID topicId;
}
