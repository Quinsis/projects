package com.example.demo.repository;

import com.example.demo.model.mongodb.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    @Query("{ 'ownerId' : ?0 }")
    List<Task> getTasksByOwnerId(Long ownerId);

    @Query("{ 'id':  ?0 }")
    Task getTaskById(String taskId);
}
