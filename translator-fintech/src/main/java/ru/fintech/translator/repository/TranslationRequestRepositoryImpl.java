package ru.fintech.translator.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import ru.fintech.translator.model.TranslationRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class TranslationRequestRepositoryImpl implements TranslationRequestRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TranslationRequestRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(TranslationRequest translationRequest) {
        String sql = "INSERT INTO translation_requests (ip_address, source_text, translated_text, request_time) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, translationRequest.ipAddress());
                ps.setString(2, translationRequest.sourceText());
                ps.setString(3, translationRequest.translatedText());
                ps.setTimestamp(4, Timestamp.valueOf(translationRequest.requestTime()));
                return ps;
            }
        });
    }

}