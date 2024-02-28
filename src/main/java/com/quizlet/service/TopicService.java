package com.quizlet.service;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.TopicMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.User;
import com.quizlet.repository.TopicRepository;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  private void checkOwner(User owner, Topic topic) {
    if (!owner.getId().equals(topic.getOwnerId())) {
      throw new ForbiddenException("Access denied");
    }
  }

  public Topic create(JwtAuthenticationToken token, TopicReqDto dto) {
    User owner = userService.getByToken(token, false);
    if (repository.findByOwnerIdAndNameIgnoreCase(owner.getId(), dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Topic topic = topicMapper.dto2Model(dto);
    topic.setOwnerId(owner.getId());
    return repository.save(topic);
  }

  public Topic updateById(JwtAuthenticationToken token, UUID id, TopicReqDto dto) {
    User owner = userService.getByToken(token, false);
    Topic topic = this.getById(id, false);
    checkOwner(owner, topic);
    if (!topic.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByOwnerIdAndNameIgnoreCase(owner.getId(), dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    topicMapper.updateModelFromDto(dto, topic);
    return repository.save(topic);
  }

  public void deleteById(JwtAuthenticationToken token, UUID id) {
    User owner = userService.getByToken(token, false);
    Topic topic = this.getById(id, false);
    checkOwner(owner, topic);
    super.deleteById(id);
  }

  public Topic getById(
      JwtAuthenticationToken token, UUID id, EntityGraph entityGraph, boolean noException) {
    Topic topic = this.getById(id, false);
    if (!topic.isPublic()) {
      User owner = userService.getByToken(token, false);
      checkOwner(owner, topic);
    }
    return super.getById(id, entityGraph, noException);
  }

  public Set<Topic> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }

  public Page<Topic> getList(JwtAuthenticationToken token, List<String> filter, Pageable pageable) {
    User owner = userService.getByToken(token, false);
    UUID ownerId = owner.getId();
    filter.add("ownerId=".concat(ownerId.toString()));
    return super.getList(filter, pageable);
  }
}
