package ru.fintech.translator.repository;

import ru.fintech.translator.model.TranslationRequest;

public interface TranslationRequestRepository {
    void save(TranslationRequest translationRequest);
}
