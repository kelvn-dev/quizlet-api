package com.quizlet.config;

import com.quizlet.converter.AclConverter;
import com.quizlet.converter.ContentDispositionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(RestController.class));
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new ContentDispositionConverter());
    registry.addConverter(new AclConverter());
    WebMvcConfigurer.super.addFormatters(registry);
  }
}
