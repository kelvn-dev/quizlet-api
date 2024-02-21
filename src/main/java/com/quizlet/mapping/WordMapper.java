package com.quizlet.mapping;

import com.quizlet.dto.request.WordReqDto;
import com.quizlet.dto.response.WordResDto;
import com.quizlet.model.Word;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WordMapper {

  Word dto2Model(WordReqDto dto);

  WordResDto model2Dto(Word topic);

  void updateModelFromDto(WordReqDto dto, @MappingTarget Word topic);
}
