package com.quizlet.dto.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class FolderReqDto {
  @NotBlank private String name;
  @NotNull private Set<UUID> topicIds;
}
