package com.quizlet.dto.cache;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LeaderboardUserCacheDto implements Serializable {
  private String id;
  private String nickname;
  private String avatar;
  private double score;
  private int rank;
}
