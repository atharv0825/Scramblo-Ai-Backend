package com.scramblo.ai.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AIChatResponse {

    private String answer;

    private List<SourceArticleDto> sources;

}