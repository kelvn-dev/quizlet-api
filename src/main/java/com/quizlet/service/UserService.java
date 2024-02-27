package com.quizlet.service;

import com.quizlet.dto.request.PasswordReqDto;
import com.quizlet.dto.request.UserReqDto;
import com.quizlet.exception.BadRequestException;
import com.quizlet.exception.NotFoundException;
import com.quizlet.mapping.UserMapper;
import com.quizlet.model.User;
import com.quizlet.repository.UserRepository;
import com.quizlet.service.provider.Auth0Service;
import java.util.Objects;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User, UserRepository> {

  private final UserMapper userMapper;
  private final Auth0Service auth0Service;

  public UserService(UserRepository repository, UserMapper userMapper, Auth0Service auth0Service) {
    super(repository);
    this.userMapper = userMapper;
    this.auth0Service = auth0Service;
  }

  public User getByAuth0UserId(String auth0UserId, boolean noException) {
    User user = repository.findByAuth0UserId(auth0UserId).orElse(null);
    if (Objects.isNull(user) && !noException) {
      throw new NotFoundException(modelClass, "auth0UserId", auth0UserId);
    }
    return user;
  }

  public User getByToken(JwtAuthenticationToken jwtToken) {
    String auth0UserId = jwtToken.getToken().getSubject();
    User user = this.getByAuth0UserId(auth0UserId, true);
    if (Objects.isNull(user)) {
      com.auth0.json.mgmt.users.User auth0User = auth0Service.getUserById(auth0UserId);
      user = userMapper.auth02Model(auth0User);
      user = repository.save(user);
    }
    return user;
  }

  public User updateByToken(JwtAuthenticationToken jwtToken, UserReqDto dto) {
    String auth0UserId = jwtToken.getToken().getSubject();
    User user = this.getByAuth0UserId(auth0UserId, false);
    userMapper.updateModelFromDto(dto, user);
    return repository.save(user);
  }

  public void updatePassword(JwtAuthenticationToken jwtToken, PasswordReqDto dto) {
    String auth0UserId = jwtToken.getToken().getSubject();
    this.getByAuth0UserId(auth0UserId, false);
    if (!auth0UserId.startsWith("auth0")) {
      throw new BadRequestException("Account is of type social");
    }
    auth0Service.updatePassword(auth0UserId, dto.getPassword());
  }
}
