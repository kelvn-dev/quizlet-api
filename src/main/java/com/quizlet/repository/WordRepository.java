package com.quizlet.repository;

import com.quizlet.model.Word;
import com.quizlet.repository.shape.IdAndTopicId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WordRepository extends BaseRepository<Word, UUID> {
  Optional<Word> findByTopicIdAndNameIgnoreCase(UUID topicId, String name);

  List<IdAndTopicId> findByTopicIdIn(List<UUID> topicIds);
}
