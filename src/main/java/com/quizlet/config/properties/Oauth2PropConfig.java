package com.quizlet.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.oauth2")
public class Oauth2PropConfig {
  private boolean isDisabled;
  private String issuerUri;
  private String jwkSetUri;
  private String audience;
}
