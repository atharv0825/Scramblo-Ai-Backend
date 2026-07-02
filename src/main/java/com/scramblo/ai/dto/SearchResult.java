package com.scramblo.ai.dto;

public interface SearchResult {

    Long getId();

    Long getArticleId();

    String getArticleTitle();

    Integer getChunkIndex();

    String getContent();

    Double getDistance();
}