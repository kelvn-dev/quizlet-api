package com.quizlet.service;

import com.quizlet.dto.request.UserScoreReqDto;
import com.quizlet.mapping.UserScoreMapper;
import com.quizlet.model.UserScore;
import com.quizlet.repository.UserScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserScoreService {

  private final UserScoreMapper userScoreMapper;
  private final UserScoreRepository userScoreRepository;

  public UserScore create(UserScoreReqDto dto) {
    UserScore userScore = userScoreMapper.dto2Model(dto);
    return userScoreRepository.save(userScore);
  }
}
