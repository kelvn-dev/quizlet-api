package com.quizlet.dto.response;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class FolderResDto {
  private UUID id;
  private String name;
  private Set<TopicResDto> topics;
}
