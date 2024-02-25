package com.quizlet.service.consumer;

import com.quizlet.dto.cache.LeaderboardCacheDto;
import com.quizlet.dto.cache.LeaderboardUserCacheDto;
import com.quizlet.model.UserScore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaderboardCacheConsumer {

  @Value("${redis.leaderboard-cache.key}")
  private String leaderboardCacheKey;

  @Value("${redis.user-score-cache.key}")
  private String userScoreCacheKey;

  private final RedisTemplate<String, String> redisScoreCacheTemplate;
  private final RedisTemplate<String, String> redisUserCacheTemplate;
  private final RedisTemplate<String, LeaderboardCacheDto> redisLeaderboardCacheTemplate;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "q.redis-leaderboard-update")
  public void leaderboardCacheConsumer(UserScore userScore) {

    // throttle leaderboard update interval
    LeaderboardCacheDto leaderBoardCache =
        redisLeaderboardCacheTemplate.opsForValue().get(leaderboardCacheKey);
    long currentTimeMs = System.currentTimeMillis();

    // check if throttle timestamp hasn't  passed just skip this update
    if (!canRefreshLeaderboard(leaderBoardCache, currentTimeMs)) {
      log.debug(
          "leaderboard cache update skipped , reason: throttle duration hasn't passed ,currentTs: {},  cacheTs: {}",
          currentTimeMs,
          !Objects.isNull(leaderBoardCache) ? leaderBoardCache.getLastModifyTimestampMs() : null);
      return;
    }

    // get current leaderboard top 10 users from sortedset and  update leaderboard cache & send this
    // update to "leaderboard" topic
    Set<ZSetOperations.TypedTuple<String>> userScoreCache =
        redisScoreCacheTemplate.opsForZSet().reverseRangeWithScores(userScoreCacheKey, 0, 9);

    // update leaderboard cache which stores only top 10 users
    LeaderboardCacheDto updatedLeaderboardCache =
        mapToLeaderboardCachDto(userScoreCache, currentTimeMs);
    redisLeaderboardCacheTemplate.opsForValue().set(leaderboardCacheKey, updatedLeaderboardCache);

    // broadcast leaderboard changes to websocket
    rabbitTemplate.convertAndSend(
        "x.leaderboard", "leaderboard-websocket-change-event", updatedLeaderboardCache);
    log.debug("leaderboard cache updated , updateTimestamp: {}", currentTimeMs);
  }

  private boolean canRefreshLeaderboard(LeaderboardCacheDto leaderBoardCache, long currentTimeMs) {
    // Calculate the timestamp threshold for throttling
    long throttleThreshold = currentTimeMs - 500;
    return Objects.isNull(leaderBoardCache)
        || throttleThreshold > leaderBoardCache.getLastModifyTimestampMs();
  }

  private LeaderboardCacheDto mapToLeaderboardCachDto(
      Set<ZSetOperations.TypedTuple<String>> userScoreCache, long currentTimeMs) {
    int rank = 0;
    List<LeaderboardUserCacheDto> users = new ArrayList<>(userScoreCache.size());
    for (ZSetOperations.TypedTuple<String> tuple : userScoreCache) {
      String userId = tuple.getValue();
      double score = tuple.getScore();
      LeaderboardUserCacheDto user =
          LeaderboardUserCacheDto.builder()
              .id(userId)
              .rank(++rank)
              .score(score)
              .username(getCachedUsernameByUserId(userId))
              .build();
      users.add(user);
    }
    return new LeaderboardCacheDto(users, currentTimeMs);
  }

  private String getCachedUsernameByUserId(String userId) {
    return redisUserCacheTemplate.opsForValue().get(userId);
  }
}
