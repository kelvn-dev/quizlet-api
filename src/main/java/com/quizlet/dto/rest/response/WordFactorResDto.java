package com.quizlet.dto.rest.response;

import java.util.UUID;
import lombok.Data;

@Data
public class WordFactorResDto {
  private UUID id;
  private String name;
  private String definition;
  private int learningCount;
}
