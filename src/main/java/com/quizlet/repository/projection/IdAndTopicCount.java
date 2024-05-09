package com.quizlet.repository.projection;

import java.util.UUID;

public interface IdAndTopicCount {
  UUID getId();

  int getTopicCount();
}
