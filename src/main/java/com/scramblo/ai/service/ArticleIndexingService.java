package com.scramblo.ai.service;

import com.pgvector.PGvector;
import com.scramblo.ai.dto.ArticlePublishedEvent;
import com.scramblo.ai.entity.ArticleEmbedding;
import com.scramblo.ai.entity.ArticleSearchDocument;
import com.scramblo.ai.repository.ArticleEmbeddingRepository;
import com.scramblo.ai.repository.ArticleSearchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ArticleIndexingService {

    private final TextChunkService textChunkService;
    private final EmbeddingService embeddingService;
    private final ArticleEmbeddingRepository repository;
    private final ArticleSearchRepository articleSearchRepository;

    public void indexArticle(ArticlePublishedEvent event) {

        List<String> chunks =
                textChunkService.chunkText(event.getContent());

        articleSearchRepository.save(
                ArticleSearchDocument.builder()
                        .articleId(event.getArticleId())
                        .title(event.getTitle())
                        .content(event.getContent())
                        .build()
        );

        log.info(
                "Article {} indexed into Elasticsearch",
                event.getArticleId()
        );

        for (int i = 0; i < chunks.size(); i++) {

            String chunk = chunks.get(i);

            List<Float> embedding =
                    embeddingService.generateEmbedding(chunk);

            log.info(
                    "Embedding dimension for chunk {} = {}",
                    i,
                    embedding.size()
            );

            float[] vector = new float[embedding.size()];

            for (int j = 0; j < embedding.size(); j++) {
                vector[j] = embedding.get(j);
            }

            repository.insertEmbedding(
                    event.getArticleId(),
                    event.getTitle(),
                    i,
                    chunk,
                    vectorToString(vector)
            );

            log.info("Chunk {} stored successfully", i);
        }

        log.info(
                "Stored {} chunks for article {}",
                chunks.size(),
                event.getArticleId()
        );
    }

    private String vectorToString(float[] vector) {

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < vector.length; i++) {

            if (i > 0) {
                sb.append(",");
            }

            sb.append(vector[i]);
        }

        sb.append("]");

        return sb.toString();
    }
}