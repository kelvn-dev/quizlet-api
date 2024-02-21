package com.quizlet.dto.response;

import java.util.Set;
import lombok.Data;

@Data
public class TopicWordResDto extends TopicResDto {
  private Set<WordResDto> words;
}
