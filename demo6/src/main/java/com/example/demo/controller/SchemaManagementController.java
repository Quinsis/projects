package com.example.demo.controller;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.service.impl.LinkServiceImpl;
import com.example.demo.service.impl.SchemaServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SchemaManagementController {
    private final LinkServiceImpl linkService;
    private final SchemaServiceImpl schemaService;
    private final UserServiceImpl userService;
    @Value("${spring.html.schema}")
    private String schemaHtml;

    @PutMapping("/createSchema")
    public ResponseEntity<Map<String, String>> createSchema(Principal principal) {
        try {
            Map<String, String> responseData = new HashMap<>();
            Long userId = userService.getUserByName(principal.getName()).get().getId();
            Schema schema = new Schema();
            schema.setName("Без названия");
            schema.setUserId(userId);
            schema.setTables(new ArrayList<>());
            schemaService.save(schema);
            responseData.put("schemaHtml", String.format(schemaHtml, schema.getId(), schema.getName()));
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка создания схемы: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteSchema")
    public ResponseEntity<String> deleteSchema(@RequestParam("schemaId") String schemaId) {
        try {
            linkService.getOnlineLinkBySchemaId(schemaId).ifPresent(linkService::deleteOnlineLink);
            linkService.getOfflineLinksBySchemaId(schemaId).ifPresent(links -> links.forEach(linkService::deleteOfflineLink));
            schemaService.getSchemaById(schemaId).ifPresent(schemaService::delete);
            return ResponseEntity.ok("Схема успешно удалена.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении схемы: " + e.getMessage());
        }
    }

    @PutMapping("/changeSchemaName")
    public ResponseEntity<String> changeSchemaName(
            @RequestParam("schemaName") String schemaName,
            @RequestParam("schemaId") String schemaId
    ) {
        try {
            return schemaService.getSchemaById(schemaId).map(schema -> {
                schema.setName(schemaName);
                schemaService.save(schema);
                return ResponseEntity.ok("Название схемы успешно изменено.");
            }).orElseGet(() -> ResponseEntity.ok("Схема с ID " + schemaId + " не найдена."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при изменении имени схемы: " + e.getMessage());
        }
    }
}
