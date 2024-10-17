package com.quizlet;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import com.quizlet.vbmda.common.repository.VbmdaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = VbmdaRepositoryFactoryBean.class)
public class QuizletApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuizletApiApplication.class, args);
  }
}
