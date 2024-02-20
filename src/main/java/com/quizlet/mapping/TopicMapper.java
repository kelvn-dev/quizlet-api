package com.quizlet.mapping;

import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.dto.response.TopicResDto;
import com.quizlet.model.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TopicMapper {

  Topic dto2Model(TopicReqDto dto);

  TopicResDto model2Dto(Topic topic);

  void updateModelFromDto(TopicReqDto dto, @MappingTarget Topic topic);
}
