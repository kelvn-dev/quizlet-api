package com.quizlet.dto.rest.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class WordFactorReqDto {
  @NotNull private List<UUID> wordIds;
}
