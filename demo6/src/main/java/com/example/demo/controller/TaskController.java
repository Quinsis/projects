package com.example.demo.controller;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Task;
import com.example.demo.service.impl.SchemaServiceImpl;
import com.example.demo.service.impl.TaskServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskServiceImpl taskService;
    private final UserServiceImpl userService;
    private final SchemaServiceImpl schemaService;

    @DeleteMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestParam("taskId") String taskId) {
        try {
            Task task = taskService.getTaskById(taskId).get();
            taskService.delete(task);
            return ResponseEntity.ok("Схема успешно удалена.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении схемы: " + e.getMessage());
        }
    }

    @PutMapping("/createTask")
    public ResponseEntity<Map<String, String>> createTask(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("query") String query,
            @RequestParam(name = "schemaId", required = false) String schemaId,
            @RequestParam(name = "taskId", required = false) String taskId,
            Principal principal) {
        try {
            Task task = new Task();

            if (taskId != null) {
                task = taskService.getTaskById(taskId).get();
                task.setName(name);
                task.setDescription(description);
                task.setRequiredQuery(query);
            } else if (schemaId != null) {
                Long userId = userService.getUserByName(principal.getName()).get().getId();
                Schema schema = schemaService.getSchemaById(schemaId).get();
                task.setOwnerId(userId);
                task.setSchema(schema);
                task.setName(name);
                task.setDescription(description);
                task.setRequiredQuery(query);
                task.setCode(UUID.randomUUID().toString());
                task.setConnectedUsers(new ArrayList<>());
            }

            taskService.save(task);
            Map<String, String> responseData = new HashMap<>();
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при создании задания: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
