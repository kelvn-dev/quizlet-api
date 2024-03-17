package com.quizlet.dto.rest.response;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class FolderTopicResDto {
  private UUID id;
  private String name;
  private Set<TopicResDto> topics;
}
