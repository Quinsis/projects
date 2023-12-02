package com.example.demo.service;

import com.example.demo.model.mongodb.Schema;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface QueryService {
    @Transactional
    Optional<String> completeQuery(String sqlQuery, Schema schema, String userName, String userPassword);
    @Transactional
    String getSavedQueryResult(String sqlQuery, Schema schema);
    @Transactional
    String getUnsavedQueryResult(String sqlQuery, Schema schema);
    @Transactional
    Optional<String> executeQuery(String sqlQuery, String userName, String userPassword);
}
