package com.example.demo.repository;

import com.example.demo.model.mongodb.OfflineLink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfflineLinkRepository extends MongoRepository<OfflineLink, String> {
    @Query("{ 'code' : ?0 }")
    Optional<OfflineLink> getOfflineLinkByCode(String code);

    @Query("{ 'schema.id' : ?0 }")
    Optional<List<OfflineLink>> getOfflineLinksBySchemaId(String schemaId);
}
