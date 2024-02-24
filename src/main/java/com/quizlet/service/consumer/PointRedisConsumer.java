package com.quizlet.service.consumer;

import com.quizlet.dto.request.PointReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointRedisConsumer {

  private final RedisTemplate<String, String> redisTemplate;

  @RabbitListener(queues = "q.redis-point-increment")
  public void increasePoint(PointReqDto dto) {
    // increment user's score in redis sortedset  collection
    String key = "leaderboard";
    redisTemplate.opsForZSet().incrementScore(key, dto.getId().toString(), dto.getPoint());

    log.debug("user with id: {} , point : {} added to redis set", dto.getId(), dto.getPoint());

    // publish record timestamp to "leaderboard_change" event
    //
    // kafkaLeaderboardChangeTemplate.send(configProperties.getBrokerProperties().getLeaderboardChangeTopic(),
    //      new LeaderBoardChangeDto(getTimestampMs(gameScore.getCreatedAt())));
    //
    //    log.debug("record with userId: {} , score : {} change timestamp published to kafka",
    // gameScore.getUserId(), score);
  }
}
