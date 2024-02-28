package com.quizlet.service;

import com.quizlet.dto.request.UserScoreReqDto;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.mapping.UserScoreMapper;
import com.quizlet.model.Topic;
import com.quizlet.model.User;
import com.quizlet.model.UserScore;
import com.quizlet.repository.UserScoreRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserScoreService {

  private final UserScoreMapper userScoreMapper;
  private final UserScoreRepository userScoreRepository;
  private final UserService userService;
  private final TopicService topicService;
  private final RabbitTemplate rabbitTemplate;

  private void checkPublicTopic(UUID topicId) {
    Topic topic = topicService.getById(topicId, false);
    if (!topic.isPublic()) {
      throw new ForbiddenException("Access denied");
    }
  }

  public UserScore create(JwtAuthenticationToken token, UserScoreReqDto dto) {
    this.checkPublicTopic(dto.getTopicId());
    User user = userService.getByToken(token, false);
    UserScore userScore = userScoreMapper.dto2Model(dto);
    userScore.setUserId(user.getId());
    userScore = userScoreRepository.save(userScore);
    rabbitTemplate.convertAndSend("x.leaderboard", "redis-score-update", userScore);
    return userScore;
  }
}
