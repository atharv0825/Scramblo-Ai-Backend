package com.scramblo.ai.repository;

import com.scramblo.ai.dto.SearchResult;
import com.scramblo.ai.entity.ArticleEmbedding;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ArticleEmbeddingRepository
        extends JpaRepository<ArticleEmbedding, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO article_embeddings
        (article_id, article_title, chunk_index, content, embedding)
        VALUES
        (:articleId, :articleTitle, :chunkIndex, :content,
         CAST(:embedding AS vector))
        """, nativeQuery = true)
    void insertEmbedding(
            @Param("articleId") Long articleId,
            @Param("articleTitle") String articleTitle,
            @Param("chunkIndex") Integer chunkIndex,
            @Param("content") String content,
            @Param("embedding") String embedding
    );

    @Query(value = """
SELECT
    id,
    article_id as articleId,
    article_title as articleTitle,
    chunk_index as chunkIndex,
    content,
    embedding <=> CAST(:embedding AS vector) as distance
FROM article_embeddings
ORDER BY distance
LIMIT :limit
""", nativeQuery = true)
    List<SearchResult> findTopRelevantChunks(
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );


    @Query(value = """
SELECT *
FROM article_embeddings
WHERE article_id IN (:articleIds)
""", nativeQuery = true)
    List<ArticleEmbedding> findByArticleIds(
            @Param("articleIds")
            List<Long> articleIds
    );
}