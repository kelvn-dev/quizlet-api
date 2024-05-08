package com.quizlet.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.quizlet.model.WordFactor;
import java.util.List;
import java.util.UUID;

public interface WordFactorRepository extends BaseRepository<WordFactor, UUID> {
  List<WordFactor> findByUserIdAndWordIdIn(String userId, List<UUID> wordIds);

  List<WordFactor> findByUserIdAndIsMarkedIsTrue(String userId, EntityGraph entityGraph);
}
