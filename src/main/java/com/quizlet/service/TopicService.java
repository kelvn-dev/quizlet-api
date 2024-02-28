package com.quizlet.service;

import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.TopicMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.User;
import com.quizlet.repository.TopicRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TopicService extends BaseService<Topic, TopicRepository> {

  private final TopicMapper topicMapper;
  private final UserService userService;

  public TopicService(
      TopicRepository repository, TopicMapper topicMapper, UserService userService) {
    super(repository);
    this.topicMapper = topicMapper;
    this.userService = userService;
  }

  public User getOwnerByToken(JwtAuthenticationToken token) {
    String auth0UserId = token.getToken().getSubject();
    User user = userService.getByAuth0UserId(auth0UserId, false);
    return user;
  }

  public void checkOwner(User owner, Topic topic) {
    if (!owner.getId().equals(topic.getOwnerId())) {
      throw new ForbiddenException("Access denied");
    }
  }

  public Topic create(JwtAuthenticationToken token, TopicReqDto dto) {
    User owner = this.getOwnerByToken(token);
    if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Topic topic = topicMapper.dto2Model(dto);
    topic.setOwnerId(owner.getId());
    return repository.save(topic);
  }

  public Topic updateById(JwtAuthenticationToken token, UUID id, TopicReqDto dto) {
    User owner = this.getOwnerByToken(token);
    Topic topic = this.getById(id, false);
    checkOwner(owner, topic);
    if (!topic.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    topicMapper.updateModelFromDto(dto, topic);
    return repository.save(topic);
  }

  public void deleteById(JwtAuthenticationToken token, UUID id) {
    User owner = this.getOwnerByToken(token);
    Topic topic = this.getById(id, false);
    checkOwner(owner, topic);
    super.deleteById(id);
  }

  public Set<Topic> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }
}
