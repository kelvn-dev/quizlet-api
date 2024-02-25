package com.quizlet.dto.request;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class PointReqDto {
  private UUID id;
  private String name;
  private int point;
  private long timestampMs = Instant.now().getEpochSecond() * 1000;
}
