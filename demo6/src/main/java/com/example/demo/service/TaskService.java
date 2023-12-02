package com.example.demo.service;

import com.example.demo.model.mongodb.Task;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Optional<List<Task>> getTasksByOwnerId(Long ownerId);
    Optional<Task> getTaskById(String taskId);
    Task save(Task task);
    void delete(Task task);
}
