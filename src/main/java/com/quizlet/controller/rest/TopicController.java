package com.quizlet.controller.rest;

import com.quizlet.controller.SecuredRestController;
import com.quizlet.dto.rest.request.TopicReqDto;
import com.quizlet.dto.rest.response.PageResDto;
import com.quizlet.dto.rest.response.TopicResDto;
import com.quizlet.mapping.rest.TopicMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.TopicEntityGraph;
import com.quizlet.service.rest.TopicService;
import jakarta.validation.Valid;
import java.util.List;
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
  public ResponseEntity<?> getById(JwtAuthenticationToken token, @PathVariable UUID id) {
    TopicEntityGraph entityGraph = TopicEntityGraph.____().words().____.____();
    Topic topic = topicService.getById(token, id, entityGraph, false);
    return ResponseEntity.ok(topicMapper.model2Dto(topic));
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
      JwtAuthenticationToken token,
      @PageableDefault(
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          @ParameterObject
          Pageable pageable,
      @RequestParam(required = false, defaultValue = "") List<String> filter) {
    Page<Topic> topics = topicService.getList(token, filter, pageable);
    PageResDto<TopicResDto> dto = topicService.getWordCount(topics);
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/community")
  public ResponseEntity<?> getList(
      @PageableDefault(
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          @ParameterObject
          Pageable pageable) {
    List<String> filter = List.of("isPublic=true");
    Page<Topic> topics = topicService.getList(filter, pageable);
    PageResDto<TopicResDto> dto = topicService.getWordCount(topics);
    return ResponseEntity.ok(dto);
  }
}
