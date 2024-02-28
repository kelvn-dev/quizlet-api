package com.quizlet.service;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.quizlet.dto.request.FolderReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.FolderMapper;
import com.quizlet.model.Folder;
import com.quizlet.model.FolderEntityGraph;
import com.quizlet.model.Topic;
import com.quizlet.model.User;
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
  private final UserService userService;

  public FolderService(
      FolderRepository repository,
      FolderMapper folderMapper,
      TopicService topicService,
      UserService userService) {
    super(repository);
    this.folderMapper = folderMapper;
    this.topicService = topicService;
    this.userService = userService;
  }

  public User getOwnerByToken(JwtAuthenticationToken token) {
    String auth0UserId = token.getToken().getSubject();
    User user = userService.getByAuth0UserId(auth0UserId, false);
    return user;
  }

  public void checkOwner(User owner, Folder folder) {
    if (!owner.getId().equals(folder.getOwnerId())) {
      throw new ForbiddenException("Access denied");
    }
  }

  public Folder create(JwtAuthenticationToken token, FolderReqDto dto) {
    User owner = this.getOwnerByToken(token);
    if (repository.findByOwnerIdAndNameIgnoreCase(owner.getId(), dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Folder folder = folderMapper.dto2Model(dto);
    Set<Topic> topics = topicService.getAllById(dto.getTopicIds());
    folder.setTopics(topics);
    return repository.save(folder);
  }

  public Folder updateById(JwtAuthenticationToken token, UUID id, FolderReqDto dto) {
    User owner = this.getOwnerByToken(token);
    FolderEntityGraph entityGraph = FolderEntityGraph.____().topics().____.____();
    Folder folder = this.getById(id, entityGraph, false);
    checkOwner(owner, folder);
    if (!folder.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByOwnerIdAndNameIgnoreCase(owner.getId(), dto.getName()).isPresent()) {
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
    User owner = this.getOwnerByToken(token);
    Folder folder = this.getById(id, false);
    checkOwner(owner, folder);
    super.deleteById(id);
  }

  public Folder getById(
      JwtAuthenticationToken token, UUID id, EntityGraph entityGraph, boolean noException) {
    Folder folder = this.getById(id, false);
    User owner = this.getOwnerByToken(token);
    checkOwner(owner, folder);
    return super.getById(id, entityGraph, noException);
  }

  public Page<Folder> getList(
      JwtAuthenticationToken token, List<String> filter, Pageable pageable) {
    User owner = this.getOwnerByToken(token);
    UUID ownerId = owner.getId();
    filter.add("ownerId=".concat(ownerId.toString()));
    return super.getList(filter, pageable);
  }
}
