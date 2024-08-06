package ru.fintech.translator.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fintech.translator.dto.TranslationRequestDto;
import ru.fintech.translator.service.TranslationService;

@RestController
@RequestMapping("/api/translate")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @PostMapping
    public ResponseEntity<String> translate(@RequestBody TranslationRequestDto request, HttpServletRequest httpServletRequest) {
        try {
            String sourceLang = request.sourceLang();
            String targetLang = request.targetLang();
            String sourceText = request.sourceText();

            if (sourceLang == null || targetLang == null || sourceText == null || sourceText.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не корректные параматры запроса");
            }

            String ipAddress = httpServletRequest.getRemoteAddr();
            String translatedText = translationService.translate(sourceText, sourceLang, targetLang, ipAddress);
            return ResponseEntity.ok(translatedText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка");
        }
    }
}
