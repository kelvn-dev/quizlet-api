package com.quizlet.mapping.rest;

import com.quizlet.dto.rest.request.TopicReqDto;
import com.quizlet.dto.rest.response.PageResDto;
import com.quizlet.dto.rest.response.TopicResDto;
import com.quizlet.dto.rest.response.TopicWithWordFactorResDto;
import com.quizlet.model.Topic;
import java.util.List;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(
    componentModel = "spring",
    uses = {WordMapper.class})
public interface TopicMapper {

  Topic dto2Model(TopicReqDto dto);

  TopicResDto model2Dto(Topic topic);

  TopicWithWordFactorResDto model2DtoWithFactor(Topic topic);

  @Named("ignoreWord")
  @Mapping(target = "words", ignore = true)
  TopicResDto model2DtoIgnoreWord(Topic topic);

  @Mapping(source = "totalElements", target = "totalItems")
  @Mapping(source = "number", target = "pageIndex")
  @Mapping(
      source = "content",
      target = "items",
      defaultExpression = "java(java.util.Collections.emptyList())")
  PageResDto<TopicResDto> model2Dto(Page<Topic> page);

  @IterableMapping(qualifiedByName = "ignoreWord")
  List<TopicResDto> model2Dto(List<Topic> topics);

  void updateModelFromDto(TopicReqDto dto, @MappingTarget Topic topic);
}
