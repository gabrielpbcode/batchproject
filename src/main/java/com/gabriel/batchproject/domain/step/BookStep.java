package com.gabriel.batchproject.domain.step;

import com.gabriel.batchproject.domain.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class BookStep {
  private final StepBuilderFactory stepBuilderFactory;

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
