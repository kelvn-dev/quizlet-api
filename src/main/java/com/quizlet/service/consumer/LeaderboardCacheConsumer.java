package com.quizlet.service.consumer;

import com.quizlet.dto.cache.LeaderboardDto;
import com.quizlet.dto.cache.UserCacheDto;
import com.quizlet.dto.request.PointReqDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

  private final RedisTemplate<String, String> redisPointTemplate;
  private final RedisTemplate<String, String> redisUserCacheTemplate;
  private final RedisTemplate<String, LeaderboardDto> redisLeaderboardCacheTemplate;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "q.leaderboard-change-event")
  public void leaderboardCacheConsumer(PointReqDto dto) {
    // throttle leaderboard update interval
    String cacheKey = "leaderboard";
    LeaderboardDto leaderBoardCache = redisLeaderboardCacheTemplate.opsForValue().get(cacheKey);
    long currentTimeMs = System.currentTimeMillis();

    // check if throttle timestamp hasn't  passed just skip this update
    if (!canRefreshLeaderboard(leaderBoardCache, currentTimeMs)) {
      log.info(
          "leaderboard cache update skipped , reason: throttle duration hasn't passed ,currentTs: {},  cacheTs: {}",
          currentTimeMs,
          !Objects.isNull(leaderBoardCache) ? leaderBoardCache.getLastModifyTimestampMs() : null);
      return;
    }

    // get current leaderboard top 10 users from sortedset and  update leaderboard cache & send this
    // update to "leaderboard" topic
    Set<ZSetOperations.TypedTuple<String>> leaderBoard =
        redisPointTemplate.opsForZSet().reverseRangeWithScores("user_point", 0, 9);
    if (Objects.isNull(leaderBoard)) {
      log.info("leaderboard cache update skipped , reason: current leaderboard is null");
      return;
    }

    // update leaderboard cache which stores only top 10 users
    LeaderboardDto leaderBoardUpdate = mapToLeaderboardDto(leaderBoard, currentTimeMs);
    redisLeaderboardCacheTemplate.opsForValue().set(cacheKey, leaderBoardUpdate);

    // publish leaderBoard updated record to "leaderboard" kafka topic
    rabbitTemplate.convertAndSend(
        "x.leaderboard", "leaderboard-websocket-change-event", leaderBoardUpdate);
    log.info("leaderboard cache updated , updateTimestamp: {}", currentTimeMs);
  }

  private boolean canRefreshLeaderboard(LeaderboardDto leaderBoardCache, long currentTimeMs) {
    // Calculate the timestamp threshold for throttling
    long throttleThreshold = currentTimeMs - 500;
    return Objects.isNull(leaderBoardCache)
        || throttleThreshold > leaderBoardCache.getLastModifyTimestampMs();
  }

  private LeaderboardDto mapToLeaderboardDto(
      Set<ZSetOperations.TypedTuple<String>> leaderBoard, long currentTimeMs) {
    int rank = 0;
    List<UserCacheDto> userList = new ArrayList<>(leaderBoard.size());
    for (ZSetOperations.TypedTuple<String> tuple : leaderBoard) {
      String userId = tuple.getValue();
      UserCacheDto user =
          UserCacheDto.builder()
              .id(userId)
              .rank(++rank)
              .point(tuple.getScore())
              .name(getCachedNicknameByUserId(userId))
              .build();
      userList.add(user);
    }
    return new LeaderboardDto(userList, currentTimeMs);
  }

  private String getCachedNicknameByUserId(String userId) {
    return redisUserCacheTemplate.opsForValue().get(userId);
  }
}
