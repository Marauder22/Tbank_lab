package ru.fintech.translator.model;

import java.time.LocalDateTime;

public record TranslationRequest(
        String ipAddress,
        String sourceText,
        String translatedText,
        LocalDateTime requestTime
) {
}