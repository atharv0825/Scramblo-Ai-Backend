package com.scramblo.ai.dto;

import com.scramblo.ai.entity.ArticleEmbedding;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatResult {

    private String answer;

    private List<ArticleEmbedding> chunks;
}