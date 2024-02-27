package com.quizlet.controller;

import com.quizlet.dto.request.UserScoreReqDto;
import com.quizlet.model.UserScore;
import com.quizlet.service.UserScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-scores")
@RequiredArgsConstructor
public class UserScoreController {

  private final UserScoreService userScoreService;

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody UserScoreReqDto dto) {
    UserScore userScore = userScoreService.create(dto);
    return ResponseEntity.ok(userScore);
  }
}
