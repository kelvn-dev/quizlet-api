package com.quizlet.dto.rest.response;

import com.quizlet.enums.WordStatus;
import java.util.UUID;
import lombok.Data;

@Data
public class WordResDto {
  private UUID id;
  private String name;
  private String definition;
  private Boolean isMarked;
  private WordStatus status;
  private UUID topicId;
}
