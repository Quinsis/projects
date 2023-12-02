package com.example.demo.service;

import com.example.demo.model.mongodb.Schema;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SchemaService {
    Optional<List<Schema>> getSchemasByUserId(Long userId);
    Optional<Schema> getSchemaById(String id);
    Schema save(Schema schema);
    void delete(Schema schema);
}
