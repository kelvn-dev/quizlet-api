package com.quizlet.controller;

import com.quizlet.dto.cache.LeaderboardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {
  private final RedisTemplate<String, LeaderboardDto> redisLeaderboardCacheTemplate;

  @GetMapping()
  public ResponseEntity<?> getLeaderboard() {
    String cacheKey = "leaderboard";
    LeaderboardDto leaderBoardCache = redisLeaderboardCacheTemplate.opsForValue().get(cacheKey);
    return ResponseEntity.ok(leaderBoardCache);
  }
}
