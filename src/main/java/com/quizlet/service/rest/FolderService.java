package com.quizlet.service.rest;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.quizlet.dto.rest.request.FolderReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.rest.FolderMapper;
import com.quizlet.model.Folder;
import com.quizlet.model.FolderEntityGraph;
import com.quizlet.model.Topic;
import com.quizlet.repository.FolderRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class FolderService extends BaseService<Folder, FolderRepository> {

  private final FolderMapper folderMapper;
  private final TopicService topicService;

  public FolderService(
      FolderRepository repository, FolderMapper folderMapper, TopicService topicService) {
    super(repository);
    this.folderMapper = folderMapper;
    this.topicService = topicService;
  }

  private void checkOwner(String userId, Folder folder) {
    if (!folder.getOwnerId().equals(userId)) {
      throw new ForbiddenException("Access denied");
    }
  }

  public Folder create(JwtAuthenticationToken token, FolderReqDto dto) {
    String userId = token.getToken().getSubject();
    if (repository.findByOwnerIdAndNameIgnoreCase(userId, dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Folder folder = folderMapper.dto2Model(dto);
    folder.setOwnerId(userId);
    Set<Topic> topics = topicService.getAllById(dto.getTopicIds());
    folder.setTopics(topics);
    return repository.save(folder);
  }

  public Folder updateById(JwtAuthenticationToken token, UUID id, FolderReqDto dto) {
    String userId = token.getToken().getSubject();
    FolderEntityGraph entityGraph = FolderEntityGraph.____().topics().____.____();
    Folder folder = this.getById(id, entityGraph, false);
    checkOwner(userId, folder);
    if (!folder.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByOwnerIdAndNameIgnoreCase(userId, dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    folderMapper.updateModelFromDto(dto, folder);

    // Update topics
    Set<Topic> topics = folder.getTopics();
    topics.removeIf(topic -> !dto.getTopicIds().contains(topic.getId()));
    Set<UUID> topicIds = topics.stream().map(Topic::getId).collect(Collectors.toSet());
    dto.getTopicIds().removeAll(topicIds); // Find difference
    Set<Topic> topicsToAdd = topicService.getAllById(dto.getTopicIds());
    topicsToAdd.forEach(topic -> folder.getTopics().add(topic));

    return repository.save(folder);
  }

  public void deleteById(JwtAuthenticationToken token, UUID id) {
    String userId = token.getToken().getSubject();
    Folder folder = this.getById(id, false);
    checkOwner(userId, folder);
    super.deleteById(id);
  }

  public Folder getById(
      JwtAuthenticationToken token, UUID id, EntityGraph entityGraph, boolean noException) {
    Folder folder = this.getById(id, entityGraph, noException);
    String userId = token.getToken().getSubject();
    checkOwner(userId, folder);
    return folder;
  }

  public Page<Folder> getList(
      JwtAuthenticationToken token, List<String> filter, Pageable pageable) {
    String userId = token.getToken().getSubject();
    filter.add("ownerId=".concat(userId));
    return super.getList(filter, pageable);
  }
}
