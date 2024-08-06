package ru.fintech.translator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TranslationResponse(
        @JsonProperty("translatedText")
        String translatedText
) {
}
