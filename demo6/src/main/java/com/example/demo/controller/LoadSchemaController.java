package com.example.demo.controller;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.service.impl.SchemaServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Utils;

import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class LoadSchemaController {
    private final SchemaServiceImpl schemaService;
    private final Utils utils;

    @PostMapping("/loadSchemas")
    public ResponseEntity<Map<String, String>> loadSchemas(Principal principal) {
        return utils.loadSchemasInternal(principal, false);
    }

    @PostMapping("/loadSchemasForConnect")
    public ResponseEntity<Map<String, String>> loadSchemasForConnect(Principal principal) {
        return utils.loadSchemasInternal(principal, true);
    }

    @PostMapping("/loadTablesBySchemaId")
    public ResponseEntity<Map<String, String>> loadTablesBySchemaId(@RequestParam("schemaId") String schemaId) {
        try {
            Schema schema = schemaService.getSchemaById(schemaId).get();

            Map<String, String> responseData = new HashMap<>();

            String tablesHtml = "<div class='table-header'>" +
                    "<span id='outputs-schema_name'>" + schema.getName() + "</span>" +
                    "<i onclick='shareModalOpen()' id='share_" + schemaId + "' class='fa-solid fa-cloud-arrow-up'></i>" +
                    "</div>";
            tablesHtml += "<ul class='tables'>";

            tablesHtml += utils.loadTablesInternal(schema);

            responseData.put("tables", tablesHtml);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке таблиц: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
