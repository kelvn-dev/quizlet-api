package com.quizlet.controller.rest;

import com.quizlet.controller.SecuredRestController;
import com.quizlet.dto.rest.request.MarkWordReqDto;
import com.quizlet.mapping.rest.WordFactorMapper;
import com.quizlet.model.WordFactor;
import com.quizlet.service.rest.WordFactorService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/word-factors")
@RequiredArgsConstructor
public class WordFactorController implements SecuredRestController {

  private final WordFactorService wordFactorService;
  private final WordFactorMapper wordFactorMapper;

  @PostMapping("/marking")
  public ResponseEntity<?> markWords(
      JwtAuthenticationToken token, @Valid @RequestBody MarkWordReqDto dto) {
    wordFactorService.markWords(token, dto);
    return ResponseEntity.ok(null);
  }

  @PostMapping("/unmarking")
  public ResponseEntity<?> unmarkWords(
      JwtAuthenticationToken token, @Valid @RequestBody MarkWordReqDto dto) {
    wordFactorService.unmarkWords(token, dto);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/marking")
  public ResponseEntity<?> getMarkedWords(JwtAuthenticationToken token) {
    List<WordFactor> wordFactors = wordFactorService.getMarkedWords(token);
    return ResponseEntity.ok(wordFactorMapper.model2Dto(wordFactors));
  }
}
