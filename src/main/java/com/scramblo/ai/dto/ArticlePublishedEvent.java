package com.scramblo.ai.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticlePublishedEvent {

    private Long articleId;

    private String title;

    private String content;

    private Long authorId;
}