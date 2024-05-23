package com.quizlet.service.consumer;

import com.quizlet.config.properties.RedisPropConfig;
import com.quizlet.dto.cache.LeaderboardCacheDto;
import com.quizlet.dto.cache.LeaderboardUserCacheDto;
import com.quizlet.dto.cache.UserCacheDto;
import com.quizlet.model.UserScore;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaderboardCacheConsumer {

  private final RedisPropConfig redisPropConfig;
  private final RedisTemplate<String, String> redisScoreCacheTemplate;
  private final RedisTemplate<String, UserCacheDto> redisUserCacheTemplate;
  private final RedisTemplate<String, LeaderboardCacheDto> redisLeaderboardCacheTemplate;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "q.redis-leaderboard-update")
  public void leaderboardCacheConsumer(UserScore userScore) {
    UUID topicId = userScore.getTopicId();
    String userKey = redisPropConfig.getUserScoreCacheKey().concat(topicId.toString());
    String leaderboardKey =
        redisPropConfig.getLeaderboardCacheKey().concat(userScore.getTopicId().toString());

    // throttle leaderboard update interval
    LeaderboardCacheDto leaderBoardCache =
        redisLeaderboardCacheTemplate.opsForValue().get(leaderboardKey);
    long currentTimeMs = System.currentTimeMillis();

    // check if throttle timestamp hasn't passed just skip this update
    if (!canRefreshLeaderboard(leaderBoardCache, currentTimeMs)) {
      log.info(
          "leaderboard cache update skipped , reason: throttle duration hasn't passed ,currentTs: {},  cacheTs: {}",
          currentTimeMs,
          leaderBoardCache.getLastModifyTimestampMs());
      return;
    }

    // get current leaderboard top 10 users from sortedset
    Set<ZSetOperations.TypedTuple<String>> userScoreCache =
        redisScoreCacheTemplate.opsForZSet().reverseRangeWithScores(userKey, 0, 9);

    LeaderboardCacheDto updatedLeaderboardCache =
        mapLeaderboardCache(userScoreCache, topicId, currentTimeMs);
    if (isLeaderboardUnchanged(leaderBoardCache, updatedLeaderboardCache)) {
      log.info("leaderboard cache update skipped , reason: leaderboard cache unchanged");
      return;
    }

    // update leaderboard cache which stores only top 10 users
    redisLeaderboardCacheTemplate.opsForValue().set(leaderboardKey, updatedLeaderboardCache);

    // broadcast leaderboard changes to websocket
    rabbitTemplate.convertAndSend(
        "x.leaderboard", "websocket-leaderboard-change-event", updatedLeaderboardCache);
    log.info("leaderboard cache updated , updateTimestamp: {}", currentTimeMs);
  }

  private boolean canRefreshLeaderboard(LeaderboardCacheDto leaderBoardCache, long currentTimeMs) {
    // Calculate the timestamp threshold for throttling
    long throttleThreshold = currentTimeMs - 500;
    return Objects.isNull(leaderBoardCache)
        || throttleThreshold > leaderBoardCache.getLastModifyTimestampMs();
  }

  private boolean isLeaderboardUnchanged(
      LeaderboardCacheDto leaderboardCache, LeaderboardCacheDto updatedLeaderboardCache) {
    if (Objects.isNull(leaderboardCache)) {
      return false;
    }
    List<LeaderboardUserCacheDto> users = leaderboardCache.getUsers();
    List<LeaderboardUserCacheDto> updatedUsers = updatedLeaderboardCache.getUsers();
    int size = users.size();
    int updatedSize = updatedUsers.size();
    if (size != updatedSize) {
      return false;
    }
    for (int i = 0; i < size; i++) {
      LeaderboardUserCacheDto user = users.get(i);
      LeaderboardUserCacheDto updatedUser = updatedUsers.get(i);
      if (!user.getId().equals(updatedUser.getId()) || user.getScore() != updatedUser.getScore()) {
        return false;
      }
    }
    return true;
  }

  private LeaderboardCacheDto mapLeaderboardCache(
      Set<ZSetOperations.TypedTuple<String>> userScoreCache, UUID topicId, long currentTimeMs) {
    int rank = 0;
    List<LeaderboardUserCacheDto> users = new ArrayList<>(userScoreCache.size());
    for (ZSetOperations.TypedTuple<String> tuple : userScoreCache) {
      String userId = tuple.getValue();
      double score = tuple.getScore();
      UserCacheDto userCache = getCachedUserByUserId(userId);
      LeaderboardUserCacheDto user =
          LeaderboardUserCacheDto.builder()
              .id(userId)
              .rank(++rank)
              .score(score)
              .nickname(userCache.getNickname())
              .avatar(userCache.getAvatar())
              .build();
      users.add(user);
    }
    return new LeaderboardCacheDto(users, topicId, currentTimeMs);
  }

  private UserCacheDto getCachedUserByUserId(String userId) {
    return redisUserCacheTemplate.opsForValue().get(userId);
  }
}
