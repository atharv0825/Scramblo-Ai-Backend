package com.scramblo.ai.service;

import com.scramblo.ai.dto.SearchResult;
import com.scramblo.ai.entity.ArticleEmbedding;
import com.scramblo.ai.repository.ArticleEmbeddingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrievalService {

    private static final double SIMILARITY_THRESHOLD = 0.40;

    private final EmbeddingService embeddingService;
    private final ArticleEmbeddingRepository repository;
    private final ElasticSearchService elasticSearchService;

    public List<ArticleEmbedding> retrieveRelevantChunks(
            String question
    ) {

        List<Float> queryEmbedding =
                embeddingService.generateEmbedding(question);

        String vector =
                toVectorString(queryEmbedding);

        List<SearchResult> results =
                repository.findTopRelevantChunks(
                        vector,
                        10
                );

        if (results.isEmpty()) {
            return List.of();
        }

        double bestDistance =
                results.get(0).getDistance();

        if (bestDistance > SIMILARITY_THRESHOLD) {
            return List.of();
        }

        return results.stream()
                .map(result ->
                        ArticleEmbedding.builder()
                                .id(result.getId())
                                .articleId(result.getArticleId())
                                .articleTitle(result.getArticleTitle())
                                .chunkIndex(result.getChunkIndex())
                                .content(result.getContent())
                                .build()
                )
                .toList();
    }

    public List<ArticleEmbedding> hybridSearch(
            String question
    ) {

        List<ArticleEmbedding> vectorResults =
                retrieveRelevantChunks(question);

        List<Long> elasticIds =
                elasticSearchService.search(question)
                        .stream()
                        .map(doc -> doc.getArticleId())
                        .toList();

        if (elasticIds.isEmpty()) {
            return vectorResults;
        }

        List<ArticleEmbedding> elasticChunks =
                repository.findByArticleIds(
                        elasticIds
                );

        Map<Long, ArticleEmbedding> merged =
                new LinkedHashMap<>();

        vectorResults.forEach(chunk ->
                merged.put(
                        chunk.getArticleId(),
                        chunk
                ));

        elasticChunks.forEach(chunk ->
                merged.putIfAbsent(
                        chunk.getArticleId(),
                        chunk
                ));

        log.info(
                "Vector Results = {}",
                vectorResults.size()
        );

        log.info(
                "Elastic Results = {}",
                elasticIds.size()
        );

        return merged.values()
                .stream()
                .limit(10)
                .toList();
    }

    private String toVectorString(
            List<Float> embedding
    ) {

        return "[" +
                embedding.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","))
                + "]";
    }
}