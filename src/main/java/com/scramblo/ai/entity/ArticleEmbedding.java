package com.scramblo.ai.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "article_embeddings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleEmbedding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long articleId;

    private String articleTitle;

    private Integer chunkIndex;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(
            name = "embedding",
            columnDefinition = "vector(768)"
    )
    private String embedding;
}