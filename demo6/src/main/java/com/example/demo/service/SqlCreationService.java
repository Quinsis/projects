package com.example.demo.service;

import com.example.demo.model.mongodb.Column;
import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Table;
import jakarta.transaction.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface SqlCreationService {
    @Transactional
    void createTable(Connection connection, Schema schema, Table table) throws SQLException;

    @Transactional
    void createColumn(Connection connection, Schema schema, Table table, Column column) throws SQLException;

    @Transactional
    void createSchema(Connection connection, Schema schema) throws SQLException;

    @Transactional
    Optional<String> createUser(String userName, String userPassword, String schemaName);
}
