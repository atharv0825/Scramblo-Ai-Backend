package com.scramblo.ai.service;

import com.scramblo.ai.dto.ChatResult;
import com.scramblo.ai.entity.ArticleEmbedding;
import com.scramblo.ai.prompt.RagPromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RetrievalService retrievalService;
    private final RagPromptBuilder promptBuilder;
    private final GeminiService geminiService;

    public ChatResult askQuestion(String question) {

        List<ArticleEmbedding> chunks =
                retrievalService.hybridSearch(question);

        if (chunks.isEmpty()) {

            return new ChatResult(
                    "I could not find enough information in Scramblo articles.",
                    List.of()
            );
        }

        String prompt =
                promptBuilder.buildPrompt(
                        question,
                        chunks
                );

        String answer =
                geminiService.ask(prompt);

        return new ChatResult(
                answer,
                chunks
        );
    }
}