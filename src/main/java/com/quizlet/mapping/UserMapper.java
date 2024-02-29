package com.quizlet.mapping;

import com.quizlet.dto.cache.UserCacheDto;
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

  UserCacheDto model2Cache(User user);

  @Mapping(source = "totalElements", target = "totalItems")
  @Mapping(source = "number", target = "pageIndex")
  @Mapping(source = "content", target = "items")
  PageResDto<UserResDto> model2Dto(Page<User> page);

  List<UserResDto> model2Dto(List<User> users);

  void updateModelFromDto(UserReqDto dto, @MappingTarget User user);

  @Mapping(target = "avatar", source = "picture")
  @Mapping(
      target = "createdAt",
      expression = "java( user.getCreatedAt().toInstant().getEpochSecond() )")
  @Mapping(
      target = "updatedAt",
      expression = "java( user.getUpdatedAt().toInstant().getEpochSecond() )")
  User auth02Model(com.auth0.json.mgmt.users.User user);
}
