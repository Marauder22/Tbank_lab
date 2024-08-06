package ru.fintech.translator.dto;

public record TranslationRequestDto(
        String sourceLang,
        String targetLang,
        String sourceText
) {
}
