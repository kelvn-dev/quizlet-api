package com.quizlet.service;

import com.quizlet.dto.request.FolderReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.mapping.FolderMapper;
import com.quizlet.model.Folder;
import com.quizlet.model.Topic;
import com.quizlet.repository.FolderRepository;
import java.util.*;
import java.util.stream.Collectors;
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

  public Folder create(FolderReqDto dto) {
    if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Folder folder = folderMapper.dto2Model(dto);
    Set<Topic> topics = topicService.getAllById(dto.getTopicIds());
    folder.setTopics(topics);
    return repository.save(folder);
  }

  public Folder updateById(UUID id, FolderReqDto dto) {
    Folder folder = this.getById(id, false);
    if (!folder.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
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
}
