package com.quizlet.service;

import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.mapping.TopicMapper;
import com.quizlet.model.Topic;
import com.quizlet.repository.TopicRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TopicService extends BaseService<Topic, TopicRepository> {

  private final TopicMapper topicMapper;

  public TopicService(TopicRepository repository, TopicMapper topicMapper) {
    super(repository);
    this.topicMapper = topicMapper;
  }

  public Topic create(TopicReqDto dto) {
    if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Topic topic = topicMapper.dto2Model(dto);
    return repository.save(topic);
  }

  public Topic updateById(UUID id, TopicReqDto dto) {
    Topic topic = this.getById(id, false);
    if (!topic.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    topicMapper.updateModelFromDto(dto, topic);
    return repository.save(topic);
  }

  public Set<Topic> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }
}
