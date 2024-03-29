package com.quizlet.service.provider;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import com.auth0.net.Response;
import com.auth0.net.TokenRequest;
import com.quizlet.config.Auth0ClientConfig;
import com.quizlet.config.properties.Auth0PropConfig;
import org.springframework.stereotype.Service;

@Service
public class Auth0Service {

  private Auth0PropConfig auth0PropConfig;
  private Auth0ClientConfig auth0ClientConfig;
  private ManagementAPI managementAPI;
  private AuthAPI authAPI;

  public Auth0Service(Auth0PropConfig auth0PropConfig, Auth0ClientConfig auth0ClientConfig) {
    this.auth0PropConfig = auth0PropConfig;
    this.auth0ClientConfig = auth0ClientConfig;
    this.managementAPI = this.auth0ClientConfig.getManagementAPI();
    this.authAPI = this.auth0ClientConfig.getAuthAPI();
  }

  private <T> T executeRequest(Request<T> request) {
    try {
      Response<T> response = request.execute();
      return response.getBody();
    } catch (APIException e) {
      throw new RuntimeException(e.getDescription());
    } catch (Auth0Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public User getUserById(String userId) {
    UserFilter userFilter = new UserFilter();
    userFilter.withConnection(auth0PropConfig.getDbConnection());
    Request<User> request = managementAPI.users().get(userId, userFilter);
    return executeRequest(request);
  }

  public User updatePassword(String userId, String password) {
    User user = new User(auth0PropConfig.getDbConnection());
    user.setPassword(password.toCharArray());
    Request<User> request = managementAPI.users().update(userId, user);
    return executeRequest(request);
  }

  public TokenHolder login(String email, String password) {
    TokenRequest tokenRequest = authAPI.login(email, password.toCharArray());
    return executeRequest(tokenRequest);
  }
}
