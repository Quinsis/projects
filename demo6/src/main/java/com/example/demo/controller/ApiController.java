package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.mongodb.*;
import com.example.demo.service.ApiService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ApiController {
    private final UserService userService;
    private final ApiService apiService;

    @PostMapping("/executeQuery")
    public ResponseEntity<String> executeSqlQuery(
            @RequestParam("sqlQuery") String sqlQuery,
            @RequestParam("schemaId") String schemaId
    ) {
        try {
            Schema schema = apiService.getSchemaById(schemaId);
            if (sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
            }
            return ResponseEntity.ok(executeSql(sqlQuery, schema));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    @PostMapping("/executeQueryWithoutSave")
    public ResponseEntity<String> executeSqlQueryWithoutSave(
            @RequestParam("sqlQuery") String sqlQuery,
            @RequestParam(name = "schemaId", required = false) String schemaId,
            @RequestParam(name = "taskId", required = false) String taskId
    ) {
        try {
            Schema schema = schemaId.length() == 0 ? apiService.getTaskById(taskId).getSchema() : apiService.getSchemaById(schemaId);

            if (sqlQuery.charAt(sqlQuery.length() - 1) == ';') {
                sqlQuery = sqlQuery.substring(0, sqlQuery.length() - 1);
            }

            return ResponseEntity.ok(executeSqlWithoutSave(sqlQuery, schema));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при выполнении запроса: " + e.getMessage());
        }
    }

    private String executeSqlWithoutSave(String sqlQuery, Schema schema) {
        return apiService.executeUserSQLWithoutSave(sqlQuery, schema);
    }

    private String executeSql(String sqlQuery, Schema schema) {
        return apiService.executeUserSQL(sqlQuery, schema);
    }

    @PutMapping("/changeSchemaName")
    public ResponseEntity<String> changeSchemaName(
            @RequestParam("schemaName") String schemaName,
            @RequestParam("schemaId") String schemaId
    ) {
        try {
            Schema schema = apiService.getSchemaById(schemaId);
            schema.setName(schemaName);
            apiService.saveSchema(schema);
            return ResponseEntity.ok("Название схемы успешно изменено.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при изменении имени схемы: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteSchema")
    public ResponseEntity<String> deleteSchema(@RequestParam("schemaId") String schemaId) {
        try {
            // Удаление ссылок на схемы
            OnlineLink onlineLink = apiService.getOnlineLinkBySchemaId(schemaId);
            if (onlineLink != null) {
                apiService.deleteOnlineLink(onlineLink);
            }

            for (OfflineLink offlineLink : apiService.getOfflineLinksBySchemaId(schemaId)) {
                apiService.deleteOfflineLink(offlineLink);
            }

            Schema schema = apiService.getSchemaById(schemaId);
            apiService.deleteSchema(schema);

            return ResponseEntity.ok("Схема успешно удалена.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении схемы: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteTask")
    public ResponseEntity<String> deleteTask(@RequestParam("taskId") String taskId) {
        try {
            Task task = apiService.getTaskById(taskId);
            apiService.deleteTask(task);

            return ResponseEntity.ok("Схема успешно удалена.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении схемы: " + e.getMessage());
        }
    }

    @PutMapping("/createSchema")
    public ResponseEntity<Map<String, String>> createSchema(Principal principal) {
        try {
            Map<String, String> responseData = new HashMap<>();
            Optional<User> optionalUser = userService.getUserByName(principal.getName());

            if (optionalUser.isPresent()) {
                Long userId = optionalUser.get().getId();
                Schema schema = new Schema();
                schema.setName("Без названия");
                schema.setUserId(userId);
                schema.setTables(new ArrayList<>());
                apiService.saveSchema(schema);
                List<Schema> schemas = apiService.getSchemasByUserId(userId);
                Schema saved = schemas.get(schemas.size() - 1);

                responseData.put(
                        "schemaHtml",
                        "<li onclick='choose(this)' class='schema' id='" + saved.getId() + "'>" +
                                "<i class='fa-solid fa-table-list'></i>" +
                                "<span>" + saved.getName() + "</span>" +
                                "<div class='schemaActions'>" +
                                "<svg onclick='edit(this)' id='schemaEdit' stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' stroke-linecap='round' stroke-linejoin='round' class='icon-sm' height='1em' width='1em' xmlns='http://www.w3.org/2000/svg'><path d='M12 20h9'></path><path d='M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z'></path></svg>" +
                                "<svg onclick='deleteSchema(this)' id='schemaDelete' stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' stroke-linecap='round' stroke-linejoin='round' class='icon-sm' height='1em' width='1em' xmlns='http://www.w3.org/2000/svg'><polyline points='3 6 5 6 21 6'></polyline><path d='M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2'></path><line x1='10' y1='11' x2='10' y2='17'></line><line x1='14' y1='11' x2='14' y2='17'></line></svg>" +
                                "<i onclick='accept(this)' id='schemaAccept' class='fa-solid fa-check'></i>" +
                                "<i onclick='deny(this)' id='schemaDeny' class='fa-solid fa-xmark'></i>" +
                                "</div>" +
                                "</li>"
                );
            }
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка создания схемы: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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
                task = apiService.getTaskById(taskId);
                task.setName(name);
                task.setDescription(description);
                task.setRequiredQuery(query);
            } else if (schemaId != null) {
                Long userId = userService.getUserByName(principal.getName()).get().getId();
                Schema schema = apiService.getSchemaById(schemaId);
                task.setOwnerId(userId);
                task.setSchema(schema);
                task.setName(name);
                task.setDescription(description);
                task.setRequiredQuery(query);
                task.setCode(UUID.randomUUID().toString());
                task.setConnectedUsers(new ArrayList<>());
            }

            apiService.saveTask(task);
            Map<String, String> responseData = new HashMap<>();
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при создании задания: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loadSchemas")
    public ResponseEntity<Map<String, String>> loadSchemas(Principal principal) {
        try {
            Long userId = userService.getUserByName(principal.getName()).get().getId();
            List<Schema> schemas = apiService.getSchemasByUserId(userId);
            Collections.reverse(schemas);

            Map<String, String> responseData = new HashMap<>();

            String schemasHtml = "";
            for (Schema schema : schemas) {
                schemasHtml +=
                        "<li onclick='choose(this)' class='schema' id='" + schema.getId() + "'>" +
                            "<i class='fa-solid fa-table-list'></i>" +
                            "<span>" + schema.getName() + "</span>" +
                            "<div class='schemaActions'>" +
                                "<svg onclick='edit(this)' id='schemaEdit' stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' stroke-linecap='round' stroke-linejoin='round' class='icon-sm' height='1em' width='1em' xmlns='http://www.w3.org/2000/svg'><path d='M12 20h9'></path><path d='M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z'></path></svg>" +
                                "<svg onclick='deleteSchema(this)' id='schemaDelete' stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' stroke-linecap='round' stroke-linejoin='round' class='icon-sm' height='1em' width='1em' xmlns='http://www.w3.org/2000/svg'><polyline points='3 6 5 6 21 6'></polyline><path d='M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2'></path><line x1='10' y1='11' x2='10' y2='17'></line><line x1='14' y1='11' x2='14' y2='17'></line></svg>" +
                                "<i onclick='accept(this)' id='schemaAccept' class='fa-solid fa-check'></i>" +
                                "<i onclick='deny(this)' id='schemaDeny' class='fa-solid fa-xmark'></i>" +
                            "</div>" +
                        "</li>";
            }

            responseData.put("schemas", schemasHtml);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке схем: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loadTasks")
    public ResponseEntity<Map<String, String>> loadTasks(Principal principal) {
        try {
            Long userId = userService.getUserByName(principal.getName()).get().getId();
            List<Task> tasks = apiService.getTasksByOwnerId(userId);
            Collections.reverse(tasks);

            Map<String, String> responseData = new HashMap<>();
            String tasksHtml = "";
            for (Task task : tasks) {
                tasksHtml +=
                        "<li onclick='chooseTask(this)' class='task' id='" + task.getId() + "'>" +
                                "<i class='fa-solid fa-book'></i>" +
                                "<span>" + task.getName() + "</span>" +
                                "<div class='taskActions'>" +
                                "<svg onclick='deleteTask(this)' id='taskDelete' stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' stroke-linecap='round' stroke-linejoin='round' class='icon-sm' height='1em' width='1em' xmlns='http://www.w3.org/2000/svg'><polyline points='3 6 5 6 21 6'></polyline><path d='M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2'></path><line x1='10' y1='11' x2='10' y2='17'></line><line x1='14' y1='11' x2='14' y2='17'></line></svg>" +
                                "</div>" +
                                "</li>";
            }

            responseData.put("tasks", tasksHtml);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке схем: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loadSchemasForConnect")
    public ResponseEntity<Map<String, String>> loadSchemasForConnect(Principal principal) {
        try {
            Long userId = userService.getUserByName(principal.getName()).get().getId();
            List<Schema> schemas = apiService.getSchemasByUserId(userId);
            Collections.reverse(schemas);

            Map<String, String> responseData = new HashMap<>();
            String schemasHtml = "";
            for (Schema schema : schemas) {
                schemasHtml +=
                        "<li onclick='chooseSchema(this)' class='schema' id='" + schema.getId() + "'>" +
                                "<i class='fa-solid fa-table-list'></i>" +
                                "<span>" + schema.getName() + "</span>" +
                                "</li>";
            }

            responseData.put("schemas", schemasHtml);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке схем: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loadTablesBySchemaId")
    public ResponseEntity<Map<String, String>> loadTablesBySchemaId(@RequestParam("schemaId") String schemaId) {
        try {
            Schema schema = apiService.getSchemaById(schemaId);

            Map<String, String> responseData = new HashMap<>();

            String tablesHtml = "<div class='table-header'>" +
                                    "<span id='outputs-schema_name'>" + schema.getName() + "</span>" +
                                    "<i onclick='share()' id='share_" + schemaId + "' class='fa-solid fa-cloud-arrow-up'></i>" +
                                "</div>";
            tablesHtml += "<ul class='tables'>";
            for (Table table : schema.getTables()) {
                tablesHtml +=
                        "<li class='table'>" +
                            "<span class='tableName'>" + table.getName() + "</span>" +
                            "<ul class='tableFields'>";

                for (Column column : table.getColumns()) {
                    tablesHtml +=
                            "<li class='field'>" +
                            "<div id='field-group-name' class='fieldGroup'>" +
                                "<span class='fieldName'" +
                                "title='" + column.getName() + "'>" + column.getName() + "</span>";
                    if (column.getKeyStatus() == Column.KeyStatus.PRIMARY) {
                        tablesHtml += "<i class='fa-solid fa-key pk'></i>";
                    } else if (column.getKeyStatus() == Column.KeyStatus.FOREIGN) {
                        tablesHtml += "<i class='fa-solid fa-key fk'></i>";
                    }

                    tablesHtml += "</div>" +
                            "<span class='fieldDatatype'" +
                            "title='" + column.getDataType() + "'>" + column.getDataType() + "</span>" +
                            "</li>";
                }

                tablesHtml += "</ul>" +
                        "</li>";
            }
            tablesHtml += "</ul>";

            responseData.put("tables", tablesHtml);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке таблиц: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loadTaskById")
    public ResponseEntity<Map<String, String>> loadTaskById(@RequestParam("taskId") String taskId) {
        try {
            Task task = apiService.getTaskById(taskId);
            Schema schema = task.getSchema();

            Map<String, String> responseData = new HashMap<>();

            String tablesHtml = "<ul class='tables'>";
            for (Table table : schema.getTables()) {
                tablesHtml +=
                        "<li class='table'>" +
                                "<span class='tableName'>" + table.getName() + "</span>" +
                                "<ul class='tableFields'>";

                for (Column column : table.getColumns()) {
                    tablesHtml +=
                            "<li class='field'>" +
                                    "<div id='field-group-name' class='fieldGroup'>" +
                                    "<span class='fieldName'" +
                                    "title='" + column.getName() + "'>" + column.getName() + "</span>";
                    if (column.getKeyStatus() == Column.KeyStatus.PRIMARY) {
                        tablesHtml += "<i class='fa-solid fa-key pk'></i>";
                    } else if (column.getKeyStatus() == Column.KeyStatus.FOREIGN) {
                        tablesHtml += "<i class='fa-solid fa-key fk'></i>";
                    }

                    tablesHtml += "</div>" +
                            "<span class='fieldDatatype'" +
                            "title='" + column.getDataType() + "'>" + column.getDataType() + "</span>" +
                            "</li>";
                }

                tablesHtml += "</ul>" +
                        "</li>";
            }
            tablesHtml += "</ul>";

            String taskHtml = "<div class='taskInfo'>" +
                                "<div class='taskInfo-text'>" +
                                    "<div onclick='copyTaskCode(this)' class='taskInfo-item' title='Скопировать код задания'>" +
                                        "<i class='fa-solid fa-copy copy'></i>" +
                                        "<span class='taskInfo-label'>Код задания</span>" +
                                        "<span class='taskInfo-code'>" + task.getCode() + "</span>" +
                                    "</div>" +
                                    "<div onclick='editTaskInfoItem(this)' class='taskInfo-item' title='Редактировать'>" +
                                        "<i class='fa-regular fa-pen-to-square edit'></i>" +
                                        "<i onclick='editAccept(event)' class='fa-solid fa-check editAccept  unclickable opacity-0'></i>" +
                                        "<i onclick='editDeny(event)' class='fa-solid fa-xmark editDeny unclickable opacity-0'></i>" +
                                        "<span class='taskInfo-label'>Название</span>" +
                                        "<span class='taskInfo-name taskInfo-content'>" + task.getName() + "</span>" +
                                    "</div>" +
                                    "<div onclick='editTaskInfoItem(this)' class='taskInfo-item' title='Редактировать'>" +
                                        "<i class='fa-regular fa-pen-to-square edit unclickable'></i>" +
                                        "<i onclick='editAccept(event)' class='fa-solid fa-check editAccept unclickable opacity-0'></i>" +
                                        "<i onclick='editDeny(event)' class='fa-solid fa-xmark editDeny unclickable opacity-0'></i>" +
                                        "<span class='taskInfo-label'>Описание</span>" +
                                        "<span class='taskInfo-desc taskInfo-content'>" + task.getDescription() + "</span>" +
                                    "</div>" +
                                    "<div onclick='editTaskInfoItem(this)' class='taskInfo-item' title='Редактировать'>" +
                                        "<i class='fa-regular fa-pen-to-square edit'></i>" +
                                        "<i onclick='editAccept(event)' class='fa-solid fa-check editAccept unclickable opacity-0'></i>" +
                                        "<i onclick='editDeny(event)' class='fa-solid fa-xmark editDeny unclickable opacity-0'></i>" +
                                        "<span class='taskInfo-label'>Запрос</span>" +
                                        "<span oninput='invalidQuery()' class='taskInfo-query taskInfo-content'>" + task.getRequiredQuery() + "</span>" +
                                    "</div>" +
                                    "<div class='buttons'>" +
                                        "<span onclick='executeQuery()' id='execute-sql' class='button'>Проверить запрос</span>" +
                                        "<span onclick='saveTask()' id='edit-task' class='button'>Подтвердить изменения</span>" +
                                    "</div>" +
                                "</div>" +
                                tablesHtml +
                            "</div>";

            responseData.put("task", taskHtml);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ошибка при загрузке задания: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/shareSchema")
    public ResponseEntity<Schema> shareSchema(@RequestParam("schemaId") String schemaId) {
        try {
            Schema schema = apiService.getSchemaById(schemaId);
            return ResponseEntity.ok(schema);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/link/online")
    public String createOnlineLink(@RequestBody String schemaId) {
        OnlineLink onlineLink = apiService.getOnlineLinkBySchemaId(schemaId);

        if (onlineLink == null) {
            schemaId = schemaId.substring(0, schemaId.length() - 1);
            onlineLink = new OnlineLink();
            onlineLink.setCode(UUID.randomUUID().toString());
            onlineLink.setSchemaId(schemaId);
            apiService.saveOnlineLink(onlineLink);
        }

        return "http://localhost:8080/online/" + onlineLink.getCode();
    }

    @PostMapping("/link/offline")
    public String createOfflineLink(@RequestBody String schemaId) {
        schemaId = schemaId.substring(0, schemaId.length() - 1);
        Schema schema = apiService.getSchemaById(schemaId);
        OfflineLink offlineLink = new OfflineLink();
        offlineLink.setCode(UUID.randomUUID().toString());
        offlineLink.setSchema(schema);

        apiService.saveOfflineLink(offlineLink);
        return "http://localhost:8080/offline/" + offlineLink.getCode();
    }

    @PutMapping("/importSchema")
    public ModelAndView importSchema(Schema schema, Principal principal) {
        Long userId = userService.getUserByName(principal.getName()).get().getId();
        schema.setId(null);
        schema.setUserId(userId);
        schema.setName("Импортированная схема");
        apiService.saveSchema(schema);

        return new ModelAndView("redirect:/sandbox");
    }
}
