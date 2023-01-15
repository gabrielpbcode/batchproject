package com.gabriel.batchproject.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
  @Bean("booksdb")
  public DataSource booksdb(final DataEnvironment env) {
    final var hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(env.getUrl());
    hikariConfig.setUsername(env.getUsername());
    hikariConfig.setPassword(env.getPassword());

    return new HikariDataSource(hikariConfig);
  }
}
