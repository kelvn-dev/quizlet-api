package com.quizlet.controller;

import com.quizlet.dto.cache.LeaderboardCacheDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {
  @Value("${redis.leaderboard-cache.key}")
  private String leaderboardCacheKey;
  private final RedisTemplate<String, LeaderboardCacheDto> redisLeaderboardCacheTemplate;

  @GetMapping()
  public ResponseEntity<?> getLeaderboard() {
    LeaderboardCacheDto leaderBoardCache =
        redisLeaderboardCacheTemplate.opsForValue().get(leaderboardCacheKey);
    return ResponseEntity.ok(leaderBoardCache);
  }
}
