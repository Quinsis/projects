package com.example.demo.repository;

import com.example.demo.model.mongodb.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    @Query("{ 'ownerId' : ?0 }")
    Optional<List<Task>> getTasksByOwnerId(Long ownerId);

    @Query("{ 'id':  ?0 }")
    Optional<Task> getTaskById(String taskId);
}
