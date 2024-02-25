package com.quizlet.service.consumer;

import com.quizlet.dto.cache.LeaderboardCacheDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LeaderboardConsumer {

  private final SimpMessagingTemplate simpMessagingTemplate;

  @RabbitListener(queues = "q.websocket-leaderboard-change-event")
  public void leaderboardWsConsumer(LeaderboardCacheDto dto) {
    // broadcast leaderboard changes to the websocket broker
    simpMessagingTemplate.convertAndSend("/live-updates/leaderboard", dto);
  }
}
