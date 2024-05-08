package com.quizlet.service.rest;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.quizlet.dto.rest.request.TopicReqDto;
import com.quizlet.dto.rest.response.PageResDto;
import com.quizlet.dto.rest.response.TopicResDto;
import com.quizlet.dto.rest.response.UserResDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.rest.TopicMapper;
import com.quizlet.mapping.rest.UserMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.User;
import com.quizlet.repository.TopicRepository;
import com.quizlet.repository.UserRepository;
import com.quizlet.repository.WordRepository;
import com.quizlet.repository.projection.IdAndTopicId;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TopicService extends BaseService<Topic, TopicRepository> {

  private final TopicMapper topicMapper;
  private final UserMapper userMapper;
  private final WordRepository wordRepository;
  private final UserRepository userRepository;

  public TopicService(
      TopicRepository repository,
      TopicMapper topicMapper,
      UserMapper userMapper,
      WordRepository wordRepository,
      UserRepository userRepository) {
    super(repository);
    this.topicMapper = topicMapper;
    this.userMapper = userMapper;
    this.wordRepository = wordRepository;
    this.userRepository = userRepository;
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
    Topic topic = this.getById(id, entityGraph, noException);
    if (!topic.isPublic()) {
      String userId = token.getToken().getSubject();
      checkOwner(userId, topic);
    }
    return topic;
  }

  public Set<Topic> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }

  public Page<Topic> getList(JwtAuthenticationToken token, List<String> filter, Pageable pageable) {
    String userId = token.getToken().getSubject();
    filter.add("ownerId=".concat(userId));
    return super.getList(filter, pageable);
  }

  public PageResDto<TopicResDto> getWordCount(Page<Topic> topicPage) {
    // count words of each topic
    List<Topic> topics = topicPage.getContent();
    List<UUID> topicIds = topics.stream().map(Topic::getId).toList();
    List<IdAndTopicId> words = wordRepository.findByTopicIdIn(topicIds);

    // get owner details
    List<String> ownerIds = topics.stream().map(Topic::getOwnerId).toList();
    List<User> owners = userRepository.findAllById(ownerIds);

    // map word count to response dto
    PageResDto<TopicResDto> dto = topicMapper.model2Dto(topicPage);
    dto.getItems()
        .forEach(
            topic -> {
              UUID topicId = topic.getId();
              AtomicInteger wordCount = new AtomicInteger();
              words.forEach(
                  word -> {
                    if (word.getTopicId().equals(topicId)) {
                      wordCount.incrementAndGet();
                    }
                  });
              topic.setWordCount(wordCount.intValue());
              User owner =
                  owners.stream()
                      .filter(o -> o.getId().equals(topic.getOwnerId()))
                      .findFirst()
                      .orElse(null);
              UserResDto ownerDto = userMapper.model2Dto(owner);
              topic.setOwner(ownerDto);
            });
    return dto;
  }
}
