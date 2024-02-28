package com.quizlet.repository;

import com.quizlet.model.Word;
import java.util.Optional;
import java.util.UUID;

public interface WordRepository extends BaseRepository<Word, UUID> {
  Optional<Word> findByTopicIdAndNameIgnoreCase(UUID topicId, String name);
}
