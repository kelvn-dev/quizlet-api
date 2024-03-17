package com.quizlet.repository.projection;

import java.util.UUID;

public interface IdAndTopicId {
  UUID getId();

  UUID getTopicId();
}
