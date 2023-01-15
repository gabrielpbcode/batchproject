package com.gabriel.batchproject.domain.job;

import com.gabriel.batchproject.config.BatchConfiguration;
import com.gabriel.batchproject.config.DataBaseConfig;
import com.gabriel.batchproject.config.InMemoryEnvironment;
import com.gabriel.batchproject.domain.model.Book;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JdbcTest
@SpringBatchTest
@ContextConfiguration(classes = {
  InMemoryEnvironment.class,
  DataBaseConfig.class,
  BatchConfiguration.class,
  BookBatchTest.BatchConfigTest.class
})
@ExtendWith(SpringExtension.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class BookBatchTest {
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private DataSource inmemoryBooks;

  private JdbcOperations jdbcTemplate;

  @BeforeEach
  public void setup() {
    this.jdbcTemplate = new JdbcTemplate(this.inmemoryBooks);
  }

  @Test
  public void testJdbcItemReader() {
    final var jobExecution = this.jobLauncherTestUtils.launchStep("printBooks");

    Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

    List<Map<String, String>> results = jdbcTemplate.query("SELECT * FROM BOOKS", (rs, rowNum) -> {
      Map<String, String> item = new HashMap<>();
      item.put("id", rs.getString("id"));
      item.put("author", rs.getString("author"));
      item.put("name", rs.getString("name"));
      item.put("year", rs.getString("year"));

      return item;
    });

    final var result = results.get(0);

    Assertions.assertEquals("1", result.get("id"));
    Assertions.assertEquals("Energy and Civilization: A History", result.get("name"));
    Assertions.assertEquals("Vaclav Smil", result.get("author"));
    Assertions.assertEquals("2018", result.get("year"));
  }

  @Test
  public void testJob() throws Exception {
    final var jobParameters = new JobParametersBuilder()
      .addString("dataBaseFile", "classpath:data.sql")
      .addString("properties", "classpath:application.properties")
      .toJobParameters();

    final var jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

    Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
  }

  @Configuration
  @EnableBatchProcessing
  @RequiredArgsConstructor
  static class BatchConfigTest  {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job processBooks(final Step printBooks) {
      return jobBuilderFactory.get("processBooks")
        .incrementer(new RunIdIncrementer())
        .start(printBooks)
        .build();
    }
    @Bean
    public Step printBooks(final JdbcCursorItemReader<Book> readBooks, final ItemWriter<Book> printBooks) {
      return stepBuilderFactory.get("printBooks")
        .<Book, Book>chunk(5)
        .reader(readBooks)
        .writer(printBooks)
        .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Book> readBooks(final DataSource booksdb) {
      return new JdbcCursorItemReaderBuilder<Book>()
        .name("readBooks")
        .dataSource(booksdb)
        .sql("SELECT * FROM BOOKS")
        .beanRowMapper(Book.class)
        .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Book> showBooks() {
      return (list -> list.forEach(System.out::println));
    }
  }

}

