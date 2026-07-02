package com.scramblo.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedArticleDto {

    private Long articleId;

    private String title;

    private String slug;
}
