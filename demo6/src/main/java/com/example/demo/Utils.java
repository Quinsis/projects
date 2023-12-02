package com.example.demo;

import com.example.demo.model.mongodb.Column;
import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Table;
import com.example.demo.service.impl.SchemaServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Utils {
    private final SchemaServiceImpl schemaService;
    private final UserServiceImpl userService;
    @Value("${spring.html.schema}")
    private String schemaHtml;
    @Value("${spring.html.schemaForConnection}")
    private String schemaHtmlForConnection;

    public String loadTablesInternal(Schema schema) {
        StringBuilder tablesHtmlBuilder = new StringBuilder();
        for (Table table : schema.getTables()) {
            tablesHtmlBuilder.append("<li class='table'>" + "<span class='tableName'>").append(table.getName()).append("</span>").append("<ul class='tableFields'>");

            for (Column column : table.getColumns()) {
                tablesHtmlBuilder.append("<li class='field'>" + "<div id='field-group-name' class='fieldGroup'>" + "<span class='fieldName'" + "title='").append(column.getName()).append("'>").append(column.getName()).append("</span>");
                if (column.getKeyStatus() == Column.KeyStatus.PRIMARY) {
                    tablesHtmlBuilder.append("<i class='fa-solid fa-key pk'></i>");
                } else if (column.getKeyStatus() == Column.KeyStatus.FOREIGN) {
                    tablesHtmlBuilder.append("<i class='fa-solid fa-key fk'></i>");
                }

                tablesHtmlBuilder.append("</div>" + "<span class='fieldDatatype'" + "title='").append(column.getDataType()).append("'>").append(column.getDataType()).append("</span>").append("</li>");
            }

            tablesHtmlBuilder.append("</ul>" + "</li>");
        }
        tablesHtmlBuilder.append("</ul>");
        return tablesHtmlBuilder.toString();
    }
    public ResponseEntity<Map<String, String>> loadSchemasInternal(Principal principal, boolean forConnection) {
        try {
            List<Schema> schemas = new ArrayList<>();
            schemaService.getSchemasByUserId(userService.getUserByName(principal.getName()).get().getId())
                    .ifPresent(list -> {
                        schemas.addAll(list);
                        Collections.reverse(schemas);
                    });
            Map<String, String> responseData = new HashMap<>();
            StringBuilder schemasHtml = new StringBuilder();
            if (forConnection) {
                for (Schema schema : schemas) {
                    schemasHtml.append(String.format(schemaHtmlForConnection, schema.getId(), schema.getName()));
                }
            } else {
                for (Schema schema : schemas) {
                    schemasHtml.append(String.format(schemaHtml, schema.getId(), schema.getName()));
                }
            }
            responseData.put("schemas", schemasHtml.toString());
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке схем: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
