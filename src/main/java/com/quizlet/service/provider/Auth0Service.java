package com.quizlet.service.provider;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;
import com.auth0.net.Request;
import com.auth0.net.Response;
import com.quizlet.config.Auth0ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Auth0Service {

  @Value("${auth0.db-connection}")
  private String auth0Connection;

  private final Auth0ClientConfig auth0ClientConfig;
  private final ManagementAPI managementAPI;
  private final AuthAPI authAPI;

  public Auth0Service(Auth0ClientConfig auth0ClientConfig) {
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
    userFilter.withConnection(auth0Connection);
    Request<User> request = managementAPI.users().get(userId, userFilter);
    return executeRequest(request);
  }
}
