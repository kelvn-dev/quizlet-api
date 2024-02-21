package com.quizlet.mapping;

import com.quizlet.dto.request.TopicReqDto;
import com.quizlet.dto.response.PageResDto;
import com.quizlet.dto.response.TopicResDto;
import com.quizlet.dto.response.TopicWordResDto;
import com.quizlet.model.Topic;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(
    componentModel = "spring",
    uses = {WordMapper.class})
public interface TopicMapper {

  Topic dto2Model(TopicReqDto dto);

  TopicResDto model2Dto(Topic topic);

  @Mapping(source = "totalElements", target = "totalItems")
  @Mapping(source = "number", target = "pageIndex")
  @Mapping(source = "content", target = "items")
  PageResDto<TopicResDto> model2Dto(Page<Topic> page);

  List<TopicResDto> model2Dto(List<Topic> topics);

  TopicWordResDto model2ExtendDto(Topic topic);

  void updateModelFromDto(TopicReqDto dto, @MappingTarget Topic topic);
}
