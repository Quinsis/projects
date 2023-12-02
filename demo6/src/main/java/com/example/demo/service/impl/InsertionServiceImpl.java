package com.example.demo.service.impl;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Table;
import com.example.demo.service.InsertionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InsertionServiceImpl implements InsertionService {
    @Override
    @Transactional
    public void insertValuesIntoTable(Connection connection, Schema schema, Table table) throws SQLException {
        String query;
        for (Map<String, Object> row : table.getValues()) {
            query = "insert into schema_" + schema.getId() + "." + table.getName() + " (";

            for (String key : row.keySet()) {
                query += key + ",";
            }
            query = query.substring(0, query.length() - 1);

            query += ") values (";
            for (Object value : row.values()) {
                if (value == null) query += "null,";
                else query += "'" + value + "',";
            }
            query = query.substring(0, query.length() - 1);
            query += ")";
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
        }
    }
}
