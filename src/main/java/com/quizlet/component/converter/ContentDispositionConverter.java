package com.quizlet.component.converter;

import com.quizlet.enums.ContentDisposition;
import com.quizlet.exception.BadRequestException;
import org.springframework.core.convert.converter.Converter;

public class ContentDispositionConverter implements Converter<String, ContentDisposition> {

  @Override
  public ContentDisposition convert(String source) {
    try {
      return ContentDisposition.valueOf(source.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid Content-Disposition");
    }
  }
}
