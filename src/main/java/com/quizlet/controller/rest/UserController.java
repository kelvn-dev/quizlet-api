package com.quizlet.controller.rest;

import com.quizlet.controller.SecuredRestController;
import com.quizlet.dto.rest.request.PasswordReqDto;
import com.quizlet.dto.rest.request.UserReqDto;
import com.quizlet.mapping.rest.UserMapper;
import com.quizlet.model.User;
import com.quizlet.service.rest.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController implements SecuredRestController {

  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping("/profile")
  public ResponseEntity<?> getProfile(JwtAuthenticationToken jwtToken) {
    User user = userService.getProfile(jwtToken);
    return ResponseEntity.ok(userMapper.model2Dto(user));
  }

  @PutMapping("/profile")
  public ResponseEntity<?> updateProfile(
      JwtAuthenticationToken jwtToken, @Valid @RequestBody UserReqDto dto) {
    User user = userService.updateByToken(jwtToken, dto);
    return ResponseEntity.ok(userMapper.model2Dto(user));
  }

  @PutMapping("/password")
  public ResponseEntity<?> updatePassword(
      JwtAuthenticationToken jwtToken, @Valid @RequestBody PasswordReqDto dto) {
    userService.updatePassword(jwtToken, dto);
    return ResponseEntity.ok(null);
  }
}
