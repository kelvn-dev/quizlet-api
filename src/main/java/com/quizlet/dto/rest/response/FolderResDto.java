package com.quizlet.dto.rest.response;

import java.util.UUID;
import lombok.Data;

@Data
public class FolderResDto {
  private UUID id;
  private String name;
}
