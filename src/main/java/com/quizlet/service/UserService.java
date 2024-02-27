package com.quizlet.service;

import com.quizlet.dto.request.UserReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.mapping.UserMapper;
import com.quizlet.model.User;
import com.quizlet.repository.UserRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User, UserRepository> {

  private final UserMapper userMapper;

  public UserService(UserRepository repository, UserMapper userMapper) {
    super(repository);
    this.userMapper = userMapper;
  }

  public User create(UserReqDto dto) {
    if (repository.findByUsernameIgnoreCase(dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    User user = userMapper.dto2Model(dto);
    return repository.save(user);
  }

  public User updateById(UUID id, UserReqDto dto) {
    User user = this.getById(id, false);
    if (!user.getUsername().equalsIgnoreCase(dto.getName())) {
      if (repository.findByUsernameIgnoreCase(dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    userMapper.updateModelFromDto(dto, user);
    return repository.save(user);
  }

  public Set<User> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }
}
