package com.quizlet.repository;

import com.quizlet.model.Folder;
import com.quizlet.repository.projection.IdAndTopicCount;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends BaseRepository<Folder, UUID> {
  Optional<Folder> findByOwnerIdAndNameIgnoreCase(String ownerId, String name);

  @Query("SELECT f.id as id, SIZE(f.topics) as topicCount FROM Folder f WHERE f.id in :ids")
  List<IdAndTopicCount> findTopicCount(@Param("ids") List<UUID> ids);
}
