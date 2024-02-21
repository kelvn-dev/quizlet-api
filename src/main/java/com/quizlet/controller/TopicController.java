package com.quizlet.controller;

import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.mapping.TopicMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.TopicEntityGraph;
import com.quizlet.service.TopicService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class TopicController implements SecuredRestController {

  private final TopicService topicService;
  private final TopicMapper topicMapper;

  @PostMapping()
  public ResponseEntity<?> create(@Valid @RequestBody TopicReqDto dto) {
    Topic topic = topicService.create(dto);
    return ResponseEntity.ok(topicMapper.model2Dto(topic));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable UUID id) {
    TopicEntityGraph entityGraph = TopicEntityGraph.____().words().____.____();
    Topic topic = topicService.getById(id, entityGraph, false);
    return ResponseEntity.ok(topicMapper.model2ExtendDto(topic));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(@PathVariable UUID id, @Valid @RequestBody TopicReqDto dto) {
    Topic topic = topicService.updateById(id, dto);
    return ResponseEntity.ok(topicMapper.model2Dto(topic));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable UUID id) {
    topicService.deleteById(id);
    return ResponseEntity.ok(null);
  }
}
