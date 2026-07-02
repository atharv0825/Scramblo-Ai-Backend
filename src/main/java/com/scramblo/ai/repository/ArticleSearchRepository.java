package com.scramblo.ai.repository;

import com.scramblo.ai.entity.ArticleSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ArticleSearchRepository
        extends ElasticsearchRepository<ArticleSearchDocument, Long> {

    List<ArticleSearchDocument>
    findByTitleContainingOrContentContaining(
            String title,
            String content
    );
}