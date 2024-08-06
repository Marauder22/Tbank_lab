package ru.fintech.translator.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.fintech.translator.model.TranslationRequest;
import ru.fintech.translator.model.TranslationResponse;
import ru.fintech.translator.repository.TranslationRequestRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final RestTemplate restTemplate;
    private final TranslationRequestRepository translationRequestRepository;

    @Value("${translation.api.url}")
    private String apiUrl;

    @Value("${translation.api.key}")
    private String apiKey;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public String translate(String sourceText, String sourceLang, String targetLang, String ipAddress) {
        List<String> words = Arrays.asList(sourceText.split("\\s+"));

        List<Future<String>> futures = words.stream()
                .map(word -> executorService.submit(() -> translateWord(word, sourceLang, targetLang)))
                .collect(Collectors.toList());

        String translatedText = futures.stream()
                .map(this::getFutureResult)
                .collect(Collectors.joining(" "));

        TranslationRequest translationRequest = new TranslationRequest(
                                                                        ipAddress,
                                                                        sourceText,
                                                                        translatedText,
                                                                        LocalDateTime.now());

        translationRequestRepository.save(translationRequest);

        return translatedText;
    }

    private String translateWord(String word, String sourceLang, String targetLang) {
        String url = String.format("%s?source=%s&target=%s&q=%s&key=%s", apiUrl, sourceLang, targetLang, word, apiKey);
        try {
            TranslationResponse response = restTemplate.getForObject(url, TranslationResponse.class);
            return response != null ? response.translatedText() : word;
        } catch (Exception e) {
            e.printStackTrace();
            return word;
        }
    }


    private String getFutureResult(Future<String> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "";
        }
    }
}
