package com.quizlet.service;

import com.quizlet.dto.request.UserScoreReqDto;
import com.quizlet.mapping.UserScoreMapper;
import com.quizlet.model.User;
import com.quizlet.model.UserScore;
import com.quizlet.repository.UserScoreRepository;
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
  private final RabbitTemplate rabbitTemplate;

  public UserScore create(JwtAuthenticationToken token, UserScoreReqDto dto) {
    String auth0UserId = token.getToken().getSubject();
    User user = userService.getByAuth0UserId(auth0UserId, false);
    UserScore userScore = userScoreMapper.dto2Model(dto);
    userScore.setUserId(user.getId());
    userScore = userScoreRepository.save(userScore);
    rabbitTemplate.convertAndSend("x.leaderboard", "redis-score-update", userScore);
    return userScore;
  }
}
