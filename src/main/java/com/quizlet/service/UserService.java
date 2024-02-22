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

  private final UserMapper topicMapper;

  public UserService(UserRepository repository, UserMapper topicMapper) {
    super(repository);
    this.topicMapper = topicMapper;
  }

  public User create(UserReqDto dto) {
    if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    User topic = topicMapper.dto2Model(dto);
    return repository.save(topic);
  }

  public User updateById(UUID id, UserReqDto dto) {
    User topic = this.getById(id, false);
    if (!topic.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    topicMapper.updateModelFromDto(dto, topic);
    return repository.save(topic);
  }

  public Set<User> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }
}
