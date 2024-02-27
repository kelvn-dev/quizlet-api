package com.quizlet.controller;

import com.quizlet.mapping.UserMapper;
import com.quizlet.model.User;
import com.quizlet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements SecuredRestController {

  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping("/profile")
  public ResponseEntity<?> getProfile(JwtAuthenticationToken jwtToken) {
    User user = userService.getByToken(jwtToken);
    return ResponseEntity.ok(userMapper.model2Dto(user));
  }
}
