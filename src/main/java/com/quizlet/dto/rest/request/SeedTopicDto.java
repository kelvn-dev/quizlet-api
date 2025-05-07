package com.quizlet.dto.rest.request;

import lombok.Data;

import java.util.List;

@Data
public class SeedTopicDto {
    private String name;
    private String url;
    private boolean isPublic;
    private List<WordSeedDto> words;
}