package com.quizlet.repository;

import com.quizlet.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {
  Optional<User> findByNameIgnoreCase(String name);
}
