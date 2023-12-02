package com.example.demo.service.impl;

import com.example.demo.model.mongodb.Task;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public Optional<Task> getTaskById(String taskId) {
        return taskRepository.getTaskById(taskId);
    }
    @Override
    public Optional<List<Task>> getTasksByOwnerId(Long id) {
        return taskRepository.getTasksByOwnerId(id);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }
}
