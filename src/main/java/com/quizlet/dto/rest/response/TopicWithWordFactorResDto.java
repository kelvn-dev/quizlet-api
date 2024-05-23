package com.quizlet.dto.rest.response;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class TopicWithWordFactorResDto {
  private UUID id;
  private String name;
  private String ownerId;
  private UserResDto owner;
  private String url;
  private boolean isPublic;
  private int wordCount;
  private List<WordFactorResDto> words;
}
