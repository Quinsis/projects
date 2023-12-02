package com.example.demo.repository;

import com.example.demo.model.mongodb.OnlineLink;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnlineLinkRepository extends MongoRepository<OnlineLink, String> {
    @Query("{ 'code' : ?0 }")
    Optional<OnlineLink> getOnlineLinkByCode(String code);

    @Query("{ 'schemaId' : ?0 }")
    Optional<OnlineLink> getOnlineLinkBySchemaId(String schemaId);
}
