package com.quizlet.dto.cache;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardCacheDto implements Serializable {
  private List<LeaderboardUserCacheDto> users;
  private long lastModifyTimestampMs;
}
