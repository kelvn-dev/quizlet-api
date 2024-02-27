package com.quizlet.config;

import com.quizlet.dto.cache.LeaderboardCacheDto;
import com.quizlet.dto.cache.UserCacheDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, String> redisScoreCacheTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setDefaultSerializer(new StringRedisSerializer());
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, UserCacheDto> redisUserCacheTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, UserCacheDto> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, LeaderboardCacheDto> redisLeaderboardCacheTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, LeaderboardCacheDto> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    return redisTemplate;
  }
}
