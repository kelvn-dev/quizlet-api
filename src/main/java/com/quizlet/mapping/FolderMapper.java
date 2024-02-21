package com.quizlet.mapping;

import com.quizlet.dto.request.FolderReqDto;
import com.quizlet.dto.response.FolderResDto;
import com.quizlet.model.Folder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FolderMapper {

  Folder dto2Model(FolderReqDto dto);

  FolderResDto model2Dto(Folder folder);

  void updateModelFromDto(FolderReqDto dto, @MappingTarget Folder folder);
}
