package com.quizlet.service.consumer;

import com.quizlet.dto.request.PointReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointRedisConsumer {

  private final RedisTemplate<String, String> redisPointTemplate;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "q.redis-point-increment")
  public void increasePoint(PointReqDto dto) {
    // increment user's score in redis sortedset  collection
    String key = "user_point";
    redisPointTemplate.opsForZSet().incrementScore(key, dto.getId().toString(), dto.getPoint());

    log.info("user with id: {} , point : {} added to redis set", dto.getId(), dto.getPoint());

    // publish record timestamp to "leaderboard_change" event
    rabbitTemplate.convertAndSend("x.leaderboard", "leaderboard-change-event", dto);
  }
}
