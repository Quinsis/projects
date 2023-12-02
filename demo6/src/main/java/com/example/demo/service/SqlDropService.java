package com.example.demo.service;

import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface SqlDropService {
    @Transactional
    void rollback(String schemaId, String username);

    @Transactional
    String dropUser(String userName);

    @Transactional
    String dropSchema(String id);
}
