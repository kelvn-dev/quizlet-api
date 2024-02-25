package com.quizlet.service.consumer;

import com.quizlet.dto.request.PointReqDto;
import com.quizlet.model.User;
import com.quizlet.repository.UserRepository;
import com.quizlet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointPgConsumer {
  private final UserService userService;
  private final UserRepository userRepository;

  @RabbitListener(queues = "q.pg-point-increment")
  private void increasePoint(PointReqDto dto) {
    User user = userService.getById(dto.getId(), false);
    //    user.setPoint(user.getPoint() + dto.getPoint());
    user = userRepository.save(user);
    log.info("point updated to db , id: {}", user.getId());
  }
}
