package com.scramblo.ai.service;

import com.scramblo.ai.entity.ArticleSearchDocument;
import com.scramblo.ai.repository.ArticleSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticSearchService {

    private final ArticleSearchRepository repository;

    public List<ArticleSearchDocument> search(
            String query
    ) {

        if(query.contains(" ")) {
            return List.of();
        }

        return repository
                .findByTitleContainingOrContentContaining(
                        query,
                        query
                );
    }
}