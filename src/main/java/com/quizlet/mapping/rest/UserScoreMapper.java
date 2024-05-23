package com.quizlet.mapping.rest;

import com.quizlet.dto.rest.request.UserScoreReqDto;
import com.quizlet.model.UserScore;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserScoreMapper {
  UserScore dto2Model(UserScoreReqDto dto);
}
