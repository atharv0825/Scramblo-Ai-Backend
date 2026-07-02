package com.scramblo.ai.kafka;

import com.scramblo.ai.service.ArticleIndexingService;
import com.scramblo.ai.service.EmbeddingService;
import com.scramblo.ai.service.TextChunkService;
import com.scramblo.ai.dto.ArticlePublishedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArticleEmbeddingConsumer {

    private final ArticleIndexingService articleIndexingService;

    @KafkaListener(
            topics = "ARTICLE_PUBLISHED",
            groupId = "scramblo-ai"
    )
    public void consume(ArticlePublishedEvent event) {
        articleIndexingService.indexArticle(event);
    }
}