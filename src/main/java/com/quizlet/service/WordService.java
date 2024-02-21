package com.quizlet.service;

import com.quizlet.dto.request.WordReqDto;
import com.quizlet.exception.ConflictException;
import com.quizlet.mapping.WordMapper;
import com.quizlet.model.Word;
import com.quizlet.repository.WordRepository;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WordService extends BaseService<Word, WordRepository> {

  private final WordMapper topicMapper;

  public WordService(WordRepository repository, WordMapper topicMapper) {
    super(repository);
    this.topicMapper = topicMapper;
  }

  public Word create(WordReqDto dto) {
    if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
      throw new ConflictException(modelClass, "name", dto.getName());
    }
    Word topic = topicMapper.dto2Model(dto);
    return repository.save(topic);
  }

  public Word updateById(UUID id, WordReqDto dto) {
    Word topic = this.getById(id, false);
    if (!topic.getName().equalsIgnoreCase(dto.getName())) {
      if (repository.findByNameIgnoreCase(dto.getName()).isPresent()) {
        throw new ConflictException(modelClass, "name", dto.getName());
      }
    }
    topicMapper.updateModelFromDto(dto, topic);
    return repository.save(topic);
  }

  public Set<Word> getAllById(Set<UUID> uuids) {
    return Set.copyOf(repository.findAllById(uuids));
  }
}
