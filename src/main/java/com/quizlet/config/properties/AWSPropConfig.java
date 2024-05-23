package com.quizlet.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AWSPropConfig {
  private String keyId;
  private String secretKey;
  private String region;
  private String s3Bucket;
}
