package com.scramblo.ai.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleSearchDocument {

    @Id
    private Long articleId;

    private String title;

    private String content;
}