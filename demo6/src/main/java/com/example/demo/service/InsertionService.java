package com.example.demo.service;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Table;
import jakarta.transaction.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

public interface InsertionService {
    @Transactional
    void insertValuesIntoTable(Connection connection, Schema schema, Table table) throws SQLException;
}
