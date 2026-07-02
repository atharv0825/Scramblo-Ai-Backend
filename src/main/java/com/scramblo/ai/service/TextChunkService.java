package com.scramblo.ai.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkService {

    private static final int CHUNK_SIZE = 2000;
    private static final int OVERLAP = 200;

    public List<String> chunkText(String text) {

        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        int start = 0;

        while (start < text.length()) {

            int end = Math.min(start + CHUNK_SIZE, text.length());

            chunks.add(text.substring(start, end));

            start += (CHUNK_SIZE - OVERLAP);
        }

        return chunks;
    }
}