package com.quizlet.dto.cache;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserCacheDto implements Serializable {
  private String nickname;
  private String avatar;
}
