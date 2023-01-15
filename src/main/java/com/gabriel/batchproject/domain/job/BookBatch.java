package com.gabriel.batchproject.domain.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BookBatch {
  private final JobBuilderFactory jobBuilderFactory;

  @Bean
  public Job processBooks(final Step printBooks) {
    return jobBuilderFactory.get("processBooks")
      .incrementer(new RunIdIncrementer())
      .start(printBooks)
      .build();
  }
}
