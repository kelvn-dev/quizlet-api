package com.quizlet.controller.rest;

import com.quizlet.controller.SecuredRestController;
import com.quizlet.dto.rest.request.UserScoreReqDto;
import com.quizlet.model.UserScore;
import com.quizlet.service.rest.UserScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-scores")
@RequiredArgsConstructor
public class UserScoreController implements SecuredRestController {

  private final UserScoreService userScoreService;

  @PostMapping
  public ResponseEntity<?> create(
      JwtAuthenticationToken token, @Valid @RequestBody UserScoreReqDto dto) {
    UserScore userScore = userScoreService.create(token, dto);
    return ResponseEntity.ok(userScore);
  }
}
