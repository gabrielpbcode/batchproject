package com.gabriel.batchproject;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class BatchProjectApplication {
  public static void main(String[] args) {
    final var app = new SpringApplication(BatchProjectApplication.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.run(args);
  }
}
