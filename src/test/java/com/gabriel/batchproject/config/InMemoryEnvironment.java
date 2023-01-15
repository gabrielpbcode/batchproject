package com.gabriel.batchproject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class InMemoryEnvironment {
  @Value("${app.datasource.url}")
  private String url;

  @Value("${app.datasource.username}")
  private String username;

  @Value("${app.datasource.password}")
  private String password;

  @Value("${app.datasource.driver-class-name}")
  private String dataSourceClassName;
}
