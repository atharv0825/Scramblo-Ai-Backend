package com.scramblo.ai.controller;

import com.scramblo.ai.dto.*;
import com.scramblo.ai.entity.ArticleEmbedding;
import com.scramblo.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AIController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public AIChatResponse chat(
            @RequestBody AIChatRequest request
    ) {

        ChatResult result =
                chatService.askQuestion(
                        request.getQuestion()
                );

        if (result.getChunks().isEmpty()) {

            return AIChatResponse.builder()
                    .answer(result.getAnswer())
                    .sources(List.of())
                    .build();
        }

        List<SourceArticleDto> sources =
                result.getChunks()
                        .stream()
                        .collect(Collectors.toMap(
                                ArticleEmbedding::getArticleId,
                                chunk -> SourceArticleDto.builder()
                                        .articleId(chunk.getArticleId())
                                        .title(chunk.getArticleTitle())
                                        .build(),
                                (a, b) -> a
                        ))
                        .values()
                        .stream()
                        .toList();

        return AIChatResponse.builder()
                .answer(result.getAnswer())
                .sources(sources)
                .build();
    }
}