package com.scramblo.ai.prompt;

import com.scramblo.ai.entity.ArticleEmbedding;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RagPromptBuilder {

    public String buildPrompt(
            String question,
            List<ArticleEmbedding> chunks
    ) {

        String context = chunks.stream()
                .map(ArticleEmbedding::getContent)
                .collect(Collectors.joining("\n\n"));

        return """
                You are Scramblo AI.

                Answer ONLY using the provided context.

                If the answer is not present in the context, say:
                "I could not find enough information in Scramblo articles."

                Rules:
                - Return plain text only.
                - Do not use markdown.
                - Do not use bullet points.
                - Do not use asterisks (*).
                - Do not use bold or italic formatting.
                - Do not use headings.
                - Write answers as normal readable paragraphs.
                - Keep the response concise and professional.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);
    }
}