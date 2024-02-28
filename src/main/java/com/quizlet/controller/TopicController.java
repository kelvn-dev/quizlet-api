package com.quizlet.controller;

import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.mapping.TopicMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.TopicEntityGraph;
import com.quizlet.service.TopicService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/topics")
@RequiredArgsConstructor
public class TopicController implements SecuredRestController {

  private final TopicService topicService;
  private final TopicMapper topicMapper;

  @PostMapping()
  public ResponseEntity<?> create(
      JwtAuthenticationToken token, @Valid @RequestBody TopicReqDto dto) {
    Topic topic = topicService.create(token, dto);
    return ResponseEntity.ok(topicMapper.model2Dto(topic));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable UUID id) {
    TopicEntityGraph entityGraph = TopicEntityGraph.____().words().____.____();
    Topic topic = topicService.getById(id, entityGraph, false);
    return ResponseEntity.ok(topicMapper.model2ExtendDto(topic));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(
      JwtAuthenticationToken token, @PathVariable UUID id, @Valid @RequestBody TopicReqDto dto) {
    Topic topic = topicService.updateById(token, id, dto);
    return ResponseEntity.ok(topicMapper.model2Dto(topic));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(JwtAuthenticationToken token, @PathVariable UUID id) {
    topicService.deleteById(token, id);
    return ResponseEntity.ok(null);
  }

  @GetMapping
  public ResponseEntity<?> getList(
      @PageableDefault(
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          @ParameterObject
          Pageable pageable,
      @RequestParam(required = false) String[] filter) {
    Page<Topic> topics = topicService.getList(filter, pageable);
    return ResponseEntity.ok(topicMapper.model2Dto(topics));
  }
}
