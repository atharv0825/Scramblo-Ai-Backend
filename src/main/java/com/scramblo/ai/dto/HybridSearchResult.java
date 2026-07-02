package com.scramblo.ai.dto;


import com.scramblo.ai.entity.ArticleEmbedding;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HybridSearchResult {

    private List<ArticleEmbedding> vectorChunks;

    private List<Long> elasticArticleIds;
}
