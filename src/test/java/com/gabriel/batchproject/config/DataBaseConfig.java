package com.gabriel.batchproject.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfig {
  @Bean("inmemoryBooks")
  public DataSource inmemoryBooks(final InMemoryEnvironment memoryEnv) {
    final var hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(memoryEnv.getUrl());
    hikariConfig.setUsername(memoryEnv.getUsername());
    hikariConfig.setPassword(memoryEnv.getPassword());
    hikariConfig.setDataSourceClassName(memoryEnv.getDataSourceClassName());

    return new HikariDataSource(hikariConfig);
  }
}
