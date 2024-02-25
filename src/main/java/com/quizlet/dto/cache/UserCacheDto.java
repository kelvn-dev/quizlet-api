package com.quizlet.dto.cache;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCacheDto implements Serializable {
  private String id;
  private String name;
  private double point;
  private int rank;
}
