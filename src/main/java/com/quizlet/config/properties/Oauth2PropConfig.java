package com.quizlet.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.oauth2")
public class Oauth2PropConfig {
  private boolean disabled;
  private String issuerUri;
  private String jwkSetUri;
  private String audience;
}
