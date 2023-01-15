package com.gabriel.batchproject.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class Book {
  private BigInteger id;
  private String name;
  private String author;
  private String language;
  private String year;

  @Override
  public String toString() {
    return new StringBuilder()
      .append(id + "; ")
      .append(name + "; ")
      .append(author + "; ")
      .append(language + "; ")
      .append(year + "; ")
      .toString();
  }
}
