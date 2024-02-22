package com.quizlet.dto.request;

import lombok.Data;

@Data
public class UserReqDto {
  private String name;
  private int point;
  private int studyTime;
}
