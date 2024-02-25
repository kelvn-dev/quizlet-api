package com.quizlet.service.consumer;

import com.quizlet.model.UserScore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScoreCacheConsumer {

  @Value("${redis.user-score-cache.key}")
  private String userScoreCacheKey;

  private final RedisTemplate<String, String> redisScoreCacheTemplate;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "q.redis-score-update")
  public void updateScore(UserScore userScore) {
    // increment user's score in redis sortedset collection
    redisScoreCacheTemplate
        .opsForZSet()
        .add(userScoreCacheKey, userScore.getUserId().toString(), userScore.getScore());

    log.info(
        "user with id: {}, score : {} added to redis set",
        userScore.getUserId(),
        userScore.getScore());

    // update leaderboard cache + check timestamp
    rabbitTemplate.convertAndSend("x.leaderboard", "redis-leaderboard-update", userScore);
  }
}
