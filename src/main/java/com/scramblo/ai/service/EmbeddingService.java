package com.scramblo.ai.service;

import com.google.genai.Client;
import com.google.genai.types.EmbedContentResponse;
import com.scramblo.ai.dto.ArticlePublishedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final Client client;


    public List<Float> generateEmbedding(String text) {

        EmbedContentResponse response =
                client.models.embedContent(
                        "gemini-embedding-001",
                        text,
                        null
                );

        var embeddings = response.embeddings()
                .orElseThrow(() -> new RuntimeException("No embeddings returned"));

        if (embeddings.isEmpty()) {
            throw new RuntimeException("Empty embedding response");
        }

        return embeddings.get(0)
                .values()
                .orElseThrow(() -> new RuntimeException("Embedding values missing"));
    }


}
