package ru.fintech.translator.service;

public interface TranslationService {
    String translate(String sourceText, String sourceLang, String targetLang, String ipAddress);
}
