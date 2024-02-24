package com.quizlet.mapping;

import com.quizlet.dto.request.UserReqDto;
import com.quizlet.dto.response.PageResDto;
import com.quizlet.dto.response.UserResDto;
import com.quizlet.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User dto2Model(UserReqDto dto);

  UserResDto model2Dto(User user);

  @Mapping(source = "totalElements", target = "totalItems")
  @Mapping(source = "number", target = "pageIndex")
  @Mapping(source = "content", target = "items")
  PageResDto<UserResDto> model2Dto(Page<User> page);

  List<UserResDto> model2Dto(List<User> users);

  void updateModelFromDto(UserReqDto dto, @MappingTarget User user);
}