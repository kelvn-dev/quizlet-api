package com.quizlet.service.rest;

import com.quizlet.dto.rest.request.WordReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.rest.WordMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.Word;
import com.quizlet.model.WordEntityGraph;
import com.quizlet.repository.WordRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class WordService extends BaseService<Word, WordRepository> {

  private final WordMapper wordMapper;
  private final TopicService topicService;
  private final UserService userService;

  public WordService(
      WordRepository repository,
      WordMapper wordMapper,
      TopicService topicService,
      UserService userService) {
    super(repository);
    this.wordMapper = wordMapper;
    this.topicService = topicService;
    this.userService = userService;
  }

  private void checkOwner(String userId, Topic topic) {
    if (!userId.equals(topic.getOwnerId())) {
      throw new ForbiddenException("Access denied");
    }
  }

  public Word create(JwtAuthenticationToken token, WordReqDto dto) {
    Topic topic = topicService.getById(dto.getTopicId(), false);
    String userId = token.getToken().getSubject();
    this.checkOwner(userId, topic);
    if (repository.findByTopicIdAndNameIgnoreCase(dto.getTopicId(), dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Word word = wordMapper.dto2Model(dto);
    return repository.save(word);
  }

  public Word updateById(JwtAuthenticationToken token, UUID id, WordReqDto dto) {
    WordEntityGraph entityGraph = WordEntityGraph.____().topic().____.____();
    Word word = this.getById(id, entityGraph, false);
    Topic topic = word.getTopic();
    String userId = token.getToken().getSubject();
    this.checkOwner(userId, topic);
    if (!word.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByTopicIdAndNameIgnoreCase(dto.getTopicId(), dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    wordMapper.updateModelFromDto(dto, word);
    return repository.save(word);
  }

  public Word getById(JwtAuthenticationToken token, UUID id, boolean noException) {
    WordEntityGraph entityGraph = WordEntityGraph.____().topic().____.____();
    Word word = super.getById(id, entityGraph, noException);
    Topic topic = word.getTopic();
    if (!topic.isPublic()) {
      String userId = token.getToken().getSubject();
      this.checkOwner(userId, topic);
    }
    return word;
  }

  public void deleteById(JwtAuthenticationToken token, UUID id) {
    WordEntityGraph entityGraph = WordEntityGraph.____().topic().____.____();
    Word word = super.getById(id, entityGraph, false);
    Topic topic = word.getTopic();
    String userId = token.getToken().getSubject();
    this.checkOwner(userId, topic);
    repository.delete(word);
  }

  public Set<Word> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }
}
