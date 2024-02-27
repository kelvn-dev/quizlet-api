package com.quizlet.mapping;

import com.quizlet.dto.request.UserScoreReqDto;
import com.quizlet.model.UserScore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserScoreMapper {
  UserScore dto2Model(UserScoreReqDto dto);
}
