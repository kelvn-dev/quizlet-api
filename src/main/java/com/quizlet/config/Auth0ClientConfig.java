package com.quizlet.config;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.TokenRequest;
import java.time.Instant;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Auth0ClientConfig {

  @Value("${auth0.domain}")
  private String auth0Domain;

  @Value("${auth0.client-id}")
  private String auth0ClientId;

  @Value("${auth0.client-secret}")
  private String auth0ClientSecret;

  @Value("${auth0.management-api.audience}")
  private String audience;

  private TokenHolder tokenHolder;
  private AuthAPI authAPI;
  private ManagementAPI managementAPI;

  public AuthAPI getAuthAPI() {
    if (Objects.isNull(authAPI)) {
      authAPI = AuthAPI.newBuilder(auth0Domain, auth0ClientId, auth0ClientSecret).build();
    }
    return authAPI;
  }

  public ManagementAPI getManagementAPI() {
    if (!Objects.isNull(this.tokenHolder)
        && this.tokenHolder.getExpiresAt().getTime() > Instant.now().toEpochMilli()) {
      log.info("Token has not expired, using existing token {}", tokenHolder.toString());
      log.info("Current token expires at {}", tokenHolder.getExpiresAt().getTime());
    } else {
      log.info("Existing access token has expired");
      renewTokenHolder();
    }
    managementAPI = ManagementAPI.newBuilder(auth0Domain, tokenHolder.getAccessToken()).build();
    return managementAPI;
  }

  private void renewTokenHolder() {
    try {
      TokenRequest tokenRequest = getAuthAPI().requestToken(audience);
      tokenHolder = tokenRequest.execute().getBody();
    } catch (Auth0Exception exception) {
      throw new RuntimeException(exception.getMessage());
    }
  }
}
