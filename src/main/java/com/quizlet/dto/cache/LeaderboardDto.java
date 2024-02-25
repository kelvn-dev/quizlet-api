package com.quizlet.dto.cache;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LeaderboardDto implements Serializable {
  private List<UserCacheDto> users;
  private long lastModifyTimestampMs;
}
