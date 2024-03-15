package com.quizlet.dto.response;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class TopicWordResDto {
  private UUID id;
  private String name;
  private String url;
  private Set<WordResDto> words;
}
