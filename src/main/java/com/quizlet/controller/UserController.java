package com.quizlet.controller;

import com.quizlet.dto.request.PointReqDto;
import com.quizlet.dto.request.UserReqDto;
import com.quizlet.mapping.PointMapper;
import com.quizlet.mapping.UserMapper;
import com.quizlet.model.User;
import com.quizlet.service.UserService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements SecuredRestController {

  private final UserService userService;
  private final UserMapper userMapper;
  private final PointMapper pointMapper;
  private final RabbitTemplate rabbitTemplate;

  @PostMapping()
  public ResponseEntity<?> create(@Valid @RequestBody UserReqDto dto) {
    User user = userService.create(dto);
    return ResponseEntity.ok(userMapper.model2Dto(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable UUID id) {
    User user = userService.getById(id, false);
    return ResponseEntity.ok(userMapper.model2Dto(user));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateById(@PathVariable UUID id, @Valid @RequestBody UserReqDto dto) {
    //    User user = userService.updateById(id, dto);
    PointReqDto pointReqDto = pointMapper.userReqDto2dto(dto);
    pointReqDto.setId(id);
    rabbitTemplate.convertAndSend("x.leaderboard", "pg-point-increment", pointReqDto);
    rabbitTemplate.convertAndSend("x.leaderboard", "redis-point-increment", pointReqDto);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable UUID id) {
    userService.deleteById(id);
    return ResponseEntity.ok(null);
  }

  @GetMapping
  public ResponseEntity<?> getList(
      @PageableDefault(
              sort = {"createdAt"},
              direction = Sort.Direction.DESC)
          @ParameterObject
          Pageable pageable,
      @RequestParam(required = false) String[] filter) {
    Page<User> users = userService.getList(filter, pageable);
    return ResponseEntity.ok(userMapper.model2Dto(users));
  }
}
