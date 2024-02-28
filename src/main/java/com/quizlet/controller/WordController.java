package com.quizlet.controller;

import com.quizlet.dto.request.WordReqDto;
import com.quizlet.mapping.WordMapper;
import com.quizlet.model.Word;
import com.quizlet.service.WordService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/words")
@RequiredArgsConstructor
public class WordController implements SecuredRestController {

  private final WordService wordService;
  private final WordMapper wordMapper;

  @PostMapping()
  public ResponseEntity<?> create(@Valid @RequestBody WordReqDto dto) {
    Word word = wordService.create(dto);
    return ResponseEntity.ok(wordMapper.model2Dto(word));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable UUID id) {
    Word word = wordService.getById(id, false);
    return ResponseEntity.ok(wordMapper.model2Dto(word));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(@PathVariable UUID id, @Valid @RequestBody WordReqDto dto) {
    Word word = wordService.updateById(id, dto);
    return ResponseEntity.ok(wordMapper.model2Dto(word));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable UUID id) {
    wordService.deleteById(id);
    return ResponseEntity.ok(null);
  }

  @GetMapping
  public ResponseEntity<?> getList(
      @PageableDefault(
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          @ParameterObject
          Pageable pageable,
      @RequestParam(required = false, defaultValue = "") List<String> filter) {
    Page<Word> words = wordService.getList(filter, pageable);
    return ResponseEntity.ok(wordMapper.model2Dto(words));
  }
}
