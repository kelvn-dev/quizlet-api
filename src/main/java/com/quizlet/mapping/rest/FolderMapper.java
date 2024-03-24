package com.quizlet.mapping.rest;

import com.quizlet.dto.rest.request.FolderReqDto;
import com.quizlet.dto.rest.response.FolderResDto;
import com.quizlet.dto.rest.response.PageResDto;
import com.quizlet.model.Folder;
import java.util.List;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface FolderMapper {

  Folder dto2Model(FolderReqDto dto);

  FolderResDto model2Dto(Folder folder);

  @Named("ignoreTopic")
  @Mapping(target = "topics", ignore = true)
  FolderResDto model2DtoIgnoreTopic(Folder folder);

  @Mapping(source = "totalElements", target = "totalItems")
  @Mapping(source = "number", target = "pageIndex")
  @Mapping(
      source = "content",
      target = "items",
      defaultExpression = "java(java.util.Collections.emptyList())")
  PageResDto<FolderResDto> model2Dto(Page<Folder> page);

  @IterableMapping(qualifiedByName = "ignoreTopic")
  List<FolderResDto> model2Dto(List<Folder> folders);

  void updateModelFromDto(FolderReqDto dto, @MappingTarget Folder folder);
}
