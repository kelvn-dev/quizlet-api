package com.quizlet.controller.rest;

import com.quizlet.config.properties.RedisPropConfig;
import com.quizlet.controller.SecuredRestController;
import com.quizlet.dto.cache.LeaderboardCacheDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController implements SecuredRestController {

  private final RedisPropConfig redisPropConfig;
  private final RedisTemplate<String, LeaderboardCacheDto> redisLeaderboardCacheTemplate;

  @GetMapping("/{topicId}")
  public ResponseEntity<?> getLeaderboard(@PathVariable UUID topicId) {
    String key = redisPropConfig.getLeaderboardCacheKey().concat(topicId.toString());
    LeaderboardCacheDto leaderBoardCache = redisLeaderboardCacheTemplate.opsForValue().get(key);
    return ResponseEntity.ok(leaderBoardCache);
  }
}
