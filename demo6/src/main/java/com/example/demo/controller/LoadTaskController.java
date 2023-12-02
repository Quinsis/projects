package com.example.demo.controller;

import com.example.demo.model.mongodb.Schema;
import com.example.demo.model.mongodb.Task;
import com.example.demo.service.impl.TaskServiceImpl;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Utils;

import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class LoadTaskController {
    private final UserServiceImpl userService;
    private final TaskServiceImpl taskService;
    private final Utils utils;
    @Value("${spring.html.taskInfo}")
    private String taskInfoHtml;

    @PostMapping("/loadTasks")
    public ResponseEntity<Map<String, String>> loadTasks(Principal principal) {
        try {
            Long userId = userService.getUserByName(principal.getName()).get().getId();

            List<Task> tasks = new ArrayList<>();
            taskService.getTasksByOwnerId(userId).ifPresent(list -> {
                tasks.addAll(list);
                Collections.reverse(tasks);
            });

            Map<String, String> responseData = new HashMap<>();
            StringBuilder tasksHtml = new StringBuilder();
            for (Task task : tasks) {
                tasksHtml.append("<li onclick='chooseTask(this)' class='task' id='").append(task.getId()).append("'>")
                        .append("<i class='fa-solid fa-book'></i>")
                        .append("<span>").append(task.getName()).append("</span>")
                        .append("<div class='taskActions'>")
                        .append("<svg onclick='deleteTask(this)' id='taskDelete' stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' stroke-linecap='round' stroke-linejoin='round' class='icon-sm' height='1em' width='1em' xmlns='http://www.w3.org/2000/svg'><polyline points='3 6 5 6 21 6'></polyline><path d='M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2'></path><line x1='10' y1='11' x2='10' y2='17'></line><line x1='14' y1='11' x2='14' y2='17'></line></svg>")
                        .append("</div>")
                        .append("</li>");
            }

            responseData.put("tasks", tasksHtml.toString());
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке схем: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loadTaskById")
    public ResponseEntity<Map<String, String>> loadTaskById(@RequestParam("taskId") String taskId) {
        try {
            Task task = taskService.getTaskById(taskId).get();
            Schema schema = task.getSchema();
            Map<String, String> responseData = new HashMap<>();
            String tablesHtml = "<ul class='tables'>";
            tablesHtml += utils.loadTablesInternal(schema);
            String taskHtml = String.format(
                    taskInfoHtml, task.getCode(), task.getName(), task.getDescription(), task.getRequiredQuery(), tablesHtml);
            responseData.put("task", taskHtml);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке задания: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
