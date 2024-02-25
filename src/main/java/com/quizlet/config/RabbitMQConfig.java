package com.quizlet.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
  private final CachingConnectionFactory cachingConnectionFactory;

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RetryOperationsInterceptor retryInterceptor() {
    return RetryInterceptorBuilder.stateless()
        .maxAttempts(3)
        .backOffOptions(2000, 2.0, 10000)
        .recoverer(
            (MethodInvocationRecoverer<Void>)
                (args, cause) -> {
                  throw new AmqpRejectAndDontRequeueException(cause);
                })
        .build();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
    rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
    return rabbitTemplate;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      SimpleRabbitListenerContainerFactoryConfigurer configurer) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    configurer.configure(factory, cachingConnectionFactory);
    factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
    factory.setAdviceChain(retryInterceptor());
    return factory;
  }

  @Bean
  public Declarables createLeaderboardSchema() {
    return new Declarables(
        new DirectExchange("x.leaderboard"),
        // Redis consumer (increment point in Redis sorted set collection)
        new Queue("q.redis-score-update"),
        new Binding(
            "q.redis-score-update",
            Binding.DestinationType.QUEUE,
            "x.leaderboard",
            "redis-score-update",
            null),
        // Redis consumer (update leaderboard cache + check timestamp)
        new Queue("q.redis-leaderboard-update"),
        new Binding(
            "q.redis-leaderboard-update",
            Binding.DestinationType.QUEUE,
            "x.leaderboard",
            "redis-leaderboard-update",
            null),
        // broadcast leaderboard changes to websocket
        new Queue("q.websocket-leaderboard-change-event"),
        new Binding(
            "q.websocket-leaderboard-change-event",
            Binding.DestinationType.QUEUE,
            "x.leaderboard",
            "websocket-leaderboard-change-event",
            null));
  }
}
