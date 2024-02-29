package com.quizlet.service;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.TopicMapper;
import com.quizlet.model.Topic;
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

  public TopicService(TopicRepository repository, TopicMapper topicMapper) {
    super(repository);
    this.topicMapper = topicMapper;
  }

  private void checkOwner(String userId, Topic topic) {
    if (!topic.getOwnerId().equals(userId)) {
      throw new ForbiddenException("Access denied");
    }
  }

  public Topic create(JwtAuthenticationToken token, TopicReqDto dto) {
    String userId = token.getToken().getSubject();
    if (repository.findByOwnerIdAndNameIgnoreCase(userId, dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Topic topic = topicMapper.dto2Model(dto);
    topic.setOwnerId(userId);
    return repository.save(topic);
  }

  public Topic updateById(JwtAuthenticationToken token, UUID id, TopicReqDto dto) {
    String userId = token.getToken().getSubject();
    Topic topic = this.getById(id, false);
    checkOwner(userId, topic);
    if (!topic.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByOwnerIdAndNameIgnoreCase(userId, dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    topicMapper.updateModelFromDto(dto, topic);
    return repository.save(topic);
  }

  public void deleteById(JwtAuthenticationToken token, UUID id) {
    String userId = token.getToken().getSubject();
    Topic topic = this.getById(id, false);
    checkOwner(userId, topic);
    super.deleteById(id);
  }

  public Topic getById(
      JwtAuthenticationToken token, UUID id, EntityGraph entityGraph, boolean noException) {
    Topic topic = this.getById(id, false);
    if (!topic.isPublic()) {
      String userId = token.getToken().getSubject();
      checkOwner(userId, topic);
    }
    return super.getById(id, entityGraph, noException);
  }

  public Set<Topic> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }

  public Page<Topic> getList(JwtAuthenticationToken token, List<String> filter, Pageable pageable) {
    String userId = token.getToken().getSubject();
    filter.add("ownerId=".concat(userId));
    return super.getList(filter, pageable);
  }
}
