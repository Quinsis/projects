package com.example.demo.service;

import com.example.demo.model.mongodb.Column;
import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Table;
import jakarta.transaction.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ForeignKeyService {
    @Transactional
    void checkForeignKeys(Connection connection, Schema schema) throws SQLException;

    @Transactional
    void addForeignKeys(Connection connection, Schema schema, Table table, Column column) throws SQLException;

    @Transactional
    void setTablesWithForeignKeys(Connection connection, Schema schema, List<Table> tableWithForeignKeys) throws SQLException;

    @Transactional
    void extractForeignKeysFromTables(Connection connection, String schemaId, List<Table> tables) throws SQLException;
}
