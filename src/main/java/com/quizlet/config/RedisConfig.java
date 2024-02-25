package com.quizlet.config;

import com.quizlet.dto.cache.LeaderboardDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, String> redisPointTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, String> redisUserCacheTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, LeaderboardDto> redisLeaderboardCacheTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, LeaderboardDto> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    return redisTemplate;
  }
}
