package com.quizlet.mapping.rest;

import com.quizlet.dto.rest.response.WordFactorResDto;
import com.quizlet.model.WordFactor;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WordFactorMapper {

  @Mapping(target = "id", source = "word.id")
  @Mapping(target = "name", source = "word.name")
  @Mapping(target = "definition", source = "word.definition")
  WordFactorResDto model2Dto(WordFactor model);

  List<WordFactorResDto> model2Dto(List<WordFactor> model);
}
