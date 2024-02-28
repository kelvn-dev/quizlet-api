package com.quizlet.repository;

import com.quizlet.model.Topic;
import java.util.Optional;
import java.util.UUID;

public interface TopicRepository extends BaseRepository<Topic, UUID> {
  Optional<Topic> findByOwnerIdAndNameIgnoreCase(UUID ownerId, String name);
}
