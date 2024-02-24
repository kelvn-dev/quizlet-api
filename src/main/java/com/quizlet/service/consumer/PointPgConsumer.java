package com.quizlet.service.consumer;

import com.quizlet.dto.request.PointReqDto;
import com.quizlet.mapping.PointMapper;
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
  private final PointMapper pointMapper;

  @RabbitListener(queues = "q.point-increment")
  private void updatePoint(PointReqDto dto) {
    User user = userService.getById(dto.getId(), false);
    user.setPoint(user.getPoint() + dto.getPoint());
    user = userRepository.save(user);
    log.debug("point updated to db , id: {}", user.getId());
  }
}
