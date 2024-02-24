package com.quizlet.mapping;

import com.quizlet.dto.request.PointReqDto;
import com.quizlet.dto.request.UserReqDto;
import com.quizlet.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PointMapper {

  PointReqDto userReqDto2dto(UserReqDto dto);

  User dto2Model(PointReqDto dto);

  void updateModelFromDto(PointReqDto dto, @MappingTarget User user);
}
