package com.example.demo.repository;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Table;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemaRepository extends MongoRepository<Schema, String> {
    @Query("{ 'userId' : ?0 }")
    List<Schema> getSchemasByUserId(Long userId);

    @Query("{ 'id' : ?0 }")
    Schema getSchemaById(String id);
}
