package com.quizlet.dto.request;

import java.util.UUID;
import lombok.Data;

@Data
public class PointReqDto {
  private UUID id;
  private String name;
  private int point;
}
