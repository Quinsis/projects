package com.example.demo.controller;

import com.example.demo.model.mongodb.*;
import com.example.demo.service.impl.QueryServiceImpl;
import com.example.demo.service.impl.SchemaServiceImpl;
import com.example.demo.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SqlController {
    private final SchemaServiceImpl schemaService;
    private final TaskServiceImpl taskService;
    private final QueryServiceImpl apiService;

    @PostMapping("/executeQuery")
    public ResponseEntity<String> executeSqlQuery(
            @RequestParam("sqlQuery") String sqlQuery,
            @RequestParam("schemaId") String schemaId
    ) {
        try {
            Schema schema = schemaService.getSchemaById(schemaId).get();
            if (sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
            }
            return ResponseEntity.ok(executeSql(sqlQuery, schema));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    @PostMapping("/executeQueryWithoutSave")
    public ResponseEntity<String> executeSqlQueryWithoutSave(
            @RequestParam("sqlQuery") String sqlQuery,
            @RequestParam(name = "schemaId", required = false) String schemaId,
            @RequestParam(name = "taskId", required = false) String taskId
    ) {
        try {
            Schema schema = schemaId.length() == 0 ?
                    taskService.getTaskById(taskId).get().getSchema() : schemaService.getSchemaById(schemaId).get();

            if (sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
            }

            return ResponseEntity.ok(executeSqlWithoutSave(sqlQuery, schema));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    private String executeSqlWithoutSave(String sqlQuery, Schema schema) {
        return apiService.getUnsavedQueryResult(sqlQuery, schema);
    }

    private String executeSql(String sqlQuery, Schema schema) {
        return apiService.getSavedQueryResult(sqlQuery, schema);
    }
}
