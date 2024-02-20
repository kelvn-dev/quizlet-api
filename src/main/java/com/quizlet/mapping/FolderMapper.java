package com.quizlet.mapping;

import com.quizlet.dto.request.FolderReqDto;
import com.quizlet.model.Folder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {

  Folder dto2Model(FolderReqDto dto);
}
