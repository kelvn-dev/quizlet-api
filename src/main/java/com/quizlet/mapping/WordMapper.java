package com.quizlet.mapping;

import com.quizlet.dto.request.WordReqDto;
import com.quizlet.dto.response.PageResDto;
import com.quizlet.dto.response.WordResDto;
import com.quizlet.model.Word;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface WordMapper {

  Word dto2Model(WordReqDto dto);

  WordResDto model2Dto(Word word);

  @Mapping(source = "totalElements", target = "totalItems")
  @Mapping(source = "number", target = "pageIndex")
  @Mapping(source = "content", target = "items")
  PageResDto<WordResDto> model2Dto(Page<Word> page);

  List<WordResDto> model2Dto(List<Word> words);

  void updateModelFromDto(WordReqDto dto, @MappingTarget Word word);
}
