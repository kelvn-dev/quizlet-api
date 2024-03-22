package com.quizlet.mapping.rest;

import com.quizlet.dto.rest.request.FolderReqDto;
import com.quizlet.dto.rest.response.FolderResDto;
import com.quizlet.dto.rest.response.FolderTopicResDto;
import com.quizlet.dto.rest.response.PageResDto;
import com.quizlet.model.Folder;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface FolderMapper {

  Folder dto2Model(FolderReqDto dto);

  FolderResDto model2Dto(Folder folder);

  @Mapping(source = "totalElements", target = "totalItems")
  @Mapping(source = "number", target = "pageIndex")
  @Mapping(
      source = "content",
      target = "items",
      defaultExpression = "java(java.util.Collections.emptyList())")
  PageResDto<FolderResDto> model2Dto(Page<Folder> page);

  List<FolderResDto> model2Dto(List<Folder> folders);

  FolderTopicResDto model2ExtendDto(Folder folder);

  void updateModelFromDto(FolderReqDto dto, @MappingTarget Folder folder);
}
