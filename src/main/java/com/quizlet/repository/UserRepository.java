package com.quizlet.repository;

import com.quizlet.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {
  Optional<User> findByAuth0UserId(String auth0UserId);
}
