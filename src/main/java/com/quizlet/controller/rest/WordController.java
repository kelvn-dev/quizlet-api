package com.quizlet.controller.rest;

import com.quizlet.controller.SecuredRestController;
import com.quizlet.dto.rest.request.WordReqDto;
import com.quizlet.mapping.rest.WordMapper;
import com.quizlet.model.Word;
import com.quizlet.service.rest.WordService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/words")
@RequiredArgsConstructor
public class WordController implements SecuredRestController {

  private final WordService wordService;
  private final WordMapper wordMapper;

  @PostMapping()
  public ResponseEntity<?> create(
      JwtAuthenticationToken token, @Valid @RequestBody WordReqDto dto) {
    Word word = wordService.create(token, dto);
    return ResponseEntity.ok(wordMapper.model2Dto(word));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(JwtAuthenticationToken token, @PathVariable UUID id) {
    Word word = wordService.getById(token, id, false);
    return ResponseEntity.ok(wordMapper.model2Dto(word));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(
      JwtAuthenticationToken token, @PathVariable UUID id, @Valid @RequestBody WordReqDto dto) {
    Word word = wordService.updateById(token, id, dto);
    return ResponseEntity.ok(wordMapper.model2Dto(word));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(JwtAuthenticationToken token, @PathVariable UUID id) {
    wordService.deleteById(token, id);
    return ResponseEntity.ok(null);
  }
}
