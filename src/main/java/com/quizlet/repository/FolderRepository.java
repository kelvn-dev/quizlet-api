package com.quizlet.repository;

import com.quizlet.model.Folder;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends BaseRepository<Folder, UUID> {
  Optional<Folder> findByName(String name);
}
