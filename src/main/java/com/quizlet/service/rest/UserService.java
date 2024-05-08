package com.quizlet.service.rest;

import com.quizlet.dto.cache.UserCacheDto;
import com.quizlet.dto.rest.request.PasswordReqDto;
import com.quizlet.dto.rest.request.UserReqDto;
import com.quizlet.exception.BadRequestException;
import com.quizlet.exception.ForbiddenException;
import com.quizlet.exception.NotFoundException;
import com.quizlet.mapping.rest.UserMapper;
import com.quizlet.model.User;
import com.quizlet.repository.UserRepository;
import com.quizlet.service.provider.Auth0Service;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;
  private final UserRepository repository;
  private final Auth0Service auth0Service;
  private final RedisTemplate<String, UserCacheDto> redisUserCacheTemplate;

  public User getById(String id, boolean noException) {
    User user = repository.findById(id).orElse(null);
    if (Objects.isNull(user) && !noException) {
      throw new NotFoundException(User.class, "id", id);
    }
    return user;
  }

  public User getByToken(JwtAuthenticationToken token, boolean noException) {
    String userId = token.getToken().getSubject();
    return this.getById(userId, noException);
  }

  public User getProfile(JwtAuthenticationToken jwtToken) {
    String userId = jwtToken.getToken().getSubject();
    User user = this.getById(userId, true);
    if (Objects.isNull(user)) {
      com.auth0.json.mgmt.users.User auth0User = auth0Service.getUserById(userId);
      user = userMapper.auth02Model(auth0User);
      user = repository.save(user);
      cacheUser(user);
    }
    return user;
  }

  private void cacheUser(User user) {
    UserCacheDto dto = userMapper.model2Cache(user);
    redisUserCacheTemplate.opsForValue().set(user.getId(), dto);
  }

  private void updateUserCache(User user) {
    UserCacheDto dto = userMapper.model2Cache(user);
    redisUserCacheTemplate.opsForValue().set(user.getId(), dto);
  }

  public User updateByToken(JwtAuthenticationToken jwtToken, UserReqDto dto) {
    User user = this.getByToken(jwtToken, false);
    userMapper.updateModelFromDto(dto, user);
    updateUserCache(user);
    return repository.save(user);
  }

  public void updatePassword(JwtAuthenticationToken jwtToken, PasswordReqDto dto) {
    String userId = jwtToken.getToken().getSubject();
    if (!userId.startsWith("auth0")) {
      throw new BadRequestException("Account is of type social");
    }
    User user = this.getByToken(jwtToken, false);
    try {
      auth0Service.login(user.getEmail(), dto.getOldPassword());
    } catch (Exception exception) {
      throw new ForbiddenException("Access denied");
    }
    auth0Service.updatePassword(userId, dto.getNewPassword());
  }
}
