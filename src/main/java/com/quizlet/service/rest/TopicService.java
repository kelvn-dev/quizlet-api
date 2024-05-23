package com.quizlet.service.rest;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.quizlet.dto.rest.request.TopicReqDto;
import com.quizlet.dto.rest.response.*;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.rest.TopicMapper;
import com.quizlet.mapping.rest.UserMapper;
import com.quizlet.mapping.rest.WordFactorMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.User;
import com.quizlet.model.Word;
import com.quizlet.model.WordFactor;
import com.quizlet.repository.TopicRepository;
import com.quizlet.repository.UserRepository;
import com.quizlet.repository.WordFactorRepository;
import com.quizlet.repository.WordRepository;
import com.quizlet.repository.projection.IdAndTopicId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TopicService extends BaseService<Topic, TopicRepository> {

  private final TopicMapper topicMapper;
  private final UserMapper userMapper;
  private final WordFactorMapper wordFactorMapperr;
  private final WordRepository wordRepository;
  private final UserRepository userRepository;
  private final WordFactorRepository wordFactorRepository;

  public TopicService(
      TopicRepository repository,
      TopicMapper topicMapper,
      UserMapper userMapper,
      WordFactorMapper wordFactorMapperr,
      WordRepository wordRepository,
      UserRepository userRepository,
      WordFactorRepository wordFactorRepository) {
    super(repository);
    this.topicMapper = topicMapper;
    this.userMapper = userMapper;
    this.wordFactorMapperr = wordFactorMapperr;
    this.wordRepository = wordRepository;
    this.userRepository = userRepository;
    this.wordFactorRepository = wordFactorRepository;
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

  public TopicWithWordFactorResDto getByIdWithFactor(
      JwtAuthenticationToken token, UUID id, EntityGraph entityGraph, boolean noException) {
    Topic topic = this.getById(id, entityGraph, noException);
    String userId = token.getToken().getSubject();
    if (!topic.isPublic()) {
      checkOwner(userId, topic);
    }

    Set<Word> words = topic.getWords();
    List<UUID> wordIds = words.stream().map(Word::getId).collect(Collectors.toList());
    List<WordFactor> existingWordFactors =
        wordFactorRepository.findByUserIdAndWordIdIn(userId, wordIds);

    // Map word details to existing word factor
    existingWordFactors.forEach(
        wordFactor -> {
          wordFactor.setWord(
              words.stream()
                  .filter(w -> w.getId().equals(wordFactor.getWordId()))
                  .findFirst()
                  .orElse(null));
        });

    List<UUID> existingWordIds =
        existingWordFactors.stream().map(WordFactor::getWordId).collect(Collectors.toList());
    wordIds.removeAll(existingWordIds); // Remove existing words to get list new words to create

    // Insert non-existent word factor
    List<WordFactor> newWordFactors = new ArrayList<>();
    wordIds.forEach(
        wordId -> {
          WordFactor wordFactor = new WordFactor();
          wordFactor.setUserId(userId);
          wordFactor.setWordId(wordId);
          newWordFactors.add(wordFactor);
        });
    List<WordFactor> createdWordFactors = wordFactorRepository.saveAll(newWordFactors);

    // Map word details to newly created word factor
    createdWordFactors.forEach(
        wordFactor -> {
          wordFactor.setWord(
              words.stream()
                  .filter(w -> w.getId().equals(wordFactor.getWordId()))
                  .findFirst()
                  .orElse(null));
        });
    existingWordFactors.addAll(createdWordFactors);

    List<WordFactorResDto> wordsDto = wordFactorMapperr.model2Dto(existingWordFactors);

    TopicWithWordFactorResDto resDto = topicMapper.model2DtoWithFactor(topic);
    resDto.setWords(wordsDto);
    return resDto;
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
