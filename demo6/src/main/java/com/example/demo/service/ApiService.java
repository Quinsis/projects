package com.example.demo.service;

import com.example.demo.model.mongodb.*;
import com.example.demo.repository.OfflineLinkRepository;
import com.example.demo.repository.OnlineLinkRepository;
import com.example.demo.repository.SchemaRepository;
import com.example.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final SchemaRepository schemaRepository;
    private final OnlineLinkRepository onlineLinkRepository;
    private final OfflineLinkRepository offlineLinkRepository;
    private final TaskRepository taskRepository;
    private final JdbcTemplate jdbcTemplate;

    public List<Schema> getSchemasByUserId(Long id) {
        return schemaRepository.getSchemasByUserId(id);
    }

    public List<Task> getTasksByOwnerId(Long id) {
        return taskRepository.getTasksByOwnerId(id);
    }

    public Schema getSchemaById(String id) {
        return schemaRepository.getSchemaById(id);
    }

    @Transactional
    public void createTable(Connection connection, Schema schema, Table table) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("create table schema_" + schema.getId() + "." + table.getName() + "()");
        }
    }

    @Transactional
    public void createColumn(Connection connection, Schema schema, Table table, Column column) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "alter table schema_" + schema.getId() + "." + table.getName() + " add " +
                            column.getName() + " " + column.getDataType() +
                            (column.getKeyStatus() == Column.KeyStatus.PRIMARY ? " primary key" : "") +
                            (column.isNullable() ? " null" : (column.getKeyStatus() == Column.KeyStatus.PRIMARY ? "" : " not null"))
            );
        }
    }

    @Transactional
    public void createSchema(Connection connection, Schema schema) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("create schema schema_" + schema.getId());
        }

        for (Table table : schema.getTables()) {
            // Создаем таблицу
            createTable(connection, schema, table);

            for (Column column : table.getColumns()) {
                // Создаем таблицу
                createColumn(connection, schema, table, column);
            }
        }
    }

    @Transactional
    public void checkForeignKeys(Connection connection, Schema schema) throws SQLException {
        for (Table table : schema.getTables()) {
            for (Column column : table.getColumns()) {
                // Колонка является внешним ключом
                if (column.getForeignKeys() != null) {
                    // Добавляем внешние ключи
                    addForeignKeys(connection, schema, table, column);
                }
            }
        }
    }

    @Transactional
    public void addForeignKeys(
            Connection connection,
            Schema schema,
            Table table,
            Column column
    ) throws SQLException {
        for (String referenceTable : column.getForeignKeys().keySet()) {
            for (String referenceColumn : column.getForeignKeys().get(referenceTable)) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(
                                    "alter table schema_" + schema.getId() + "." + table.getName() +
                                    " add constraint fk_" + column.getName() + "_" + table.getName() + " " +
                                    "foreign key (" + column.getName() + ") " +
                                    "references schema_" + schema.getId() + "." + referenceTable + "(" + referenceColumn + ")");
                }
            }
        }
    }

    @Transactional
    public void setTablesWithForeignKeys(
            Connection connection,
            Schema schema,
            List<Table> tableWithForeignKeys
    ) throws SQLException {
        // Записываем данные в таблицы
        for (Table table : schema.getTables()) {
            // Проверяем наличие внешних ключей в таблице
            boolean tableHasForeignKey = false;
            for (Column column : table.getColumns()) {
                if (column.getKeyStatus() == Column.KeyStatus.FOREIGN) {
                    tableHasForeignKey = true;
                    break;
                }
            }
            if (!tableHasForeignKey) {
                insertValuesIntoTable(connection, schema, table);
            } else {
                tableWithForeignKeys.add(table);
            }
        }
    }

    @Transactional
    public void insertValuesIntoTable(Connection connection, Schema schema, Table table) throws SQLException {
        String query;
        for (Map<String, Object> row : table.getValues()) {
            query = "insert into schema_" + schema.getId() + "." + table.getName() + " (";

            for (String key : row.keySet()) {
                query += key + ",";
            }
            query = query.substring(0, query.length() - 1);

            query += ") values (";
            for (Object value : row.values()) {
                if (value == null) query += "null,";
                else query += "'" + value + "',";
            }
            query = query.substring(0, query.length() - 1);
            query += ")";
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
        }
    }

    @Transactional
    public String mongoToPostgres(Schema schema) {
        try (Connection connection = DriverManager
                .getConnection(
                        "jdbc:postgresql://localhost:5432/test",
                        "postgres",
                        "1234")
        ) {
            // 1. Добавляем схему пользователя
            createSchema(connection, schema);

            // 2. Проверяем наличие внешних ключей
            checkForeignKeys(connection, schema);

            // 3. Получаем таблицы, имеющие внешние ключи
            List<Table> tableWithForeignKeys = new ArrayList<>();
            setTablesWithForeignKeys(connection, schema, tableWithForeignKeys);

            // 4. Прогоняем цикл записи данных в таблицы с внешними ключами
            for (Table table : tableWithForeignKeys) {
                insertValuesIntoTable(connection, schema, table);
            }

            return "ok";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    @Transactional
    public void extractValuesFromTable(
            Connection connection,
            String schemaId,
            String tableName,
            Table table
    ) throws SQLException {
        try (Statement statement1 = connection.createStatement()) {
            try (ResultSet valueRow = statement1.executeQuery("select * from schema_" + schemaId + "." + tableName)) {
                while (valueRow.next()) {
                    Map<String, Object> values = new HashMap<>();
                    for (int i = 1; i <= valueRow.getMetaData().getColumnCount(); i++) {
                        String columnName = valueRow.getMetaData().getColumnName(i);
                        Object value = valueRow.getObject(i);
                        values.put(columnName, value);
                    }
                    table.getValues().add(values);
                }
            }
        }
    }

    @Transactional
    public void extractColumnsFromTable(
            Connection connection,
            String schemaId,
            String tableName,
            List<Column> columns
    ) throws SQLException {
        try (Statement statement1 = connection.createStatement()) {
            try (ResultSet columnRows = statement1.executeQuery("SELECT" +
                    "    column_name," +
                    "    data_type," +
                    "    is_nullable," +
                    "    CASE" +
                    "        WHEN column_name IN (" +
                    "            SELECT kcu.column_name" +
                    "            FROM information_schema.key_column_usage kcu" +
                    "            JOIN information_schema.table_constraints tc" +
                    "            ON kcu.constraint_name = tc.constraint_name" +
                    "            WHERE kcu.table_schema = 'schema_" + schemaId + "'" +
                    "                AND kcu.table_name = '" + tableName + "'" +
                    "                AND tc.constraint_type = 'PRIMARY KEY'" +
                    "        ) THEN 'YES'" +
                    "        ELSE 'NO'" +
                    "    END AS is_primary_key " +
                    "FROM information_schema.columns " +
                    "WHERE table_schema = 'schema_" + schemaId + "'" +
                    "    AND table_name = '" + tableName + "'")) {
                while (columnRows.next()) {
                    Column column = new Column();
                    column.setName(columnRows.getObject(1).toString());
                    column.setDataType(columnRows.getObject(2).toString());
                    column.setNullable(columnRows.getObject(3).toString().equals("YES"));
                    column.setKeyStatus(columnRows.getObject(4).toString().equals("YES")
                            ? Column.KeyStatus.PRIMARY : Column.KeyStatus.NONE);
                    column.setForeignKeys(new HashMap<>());
                    columns.add(column);
                }
            }
        }
    }

    @Transactional
    public void extractForeignKeysFromTables(Connection connection, String schemaId, List<Table> tables) throws SQLException {
        for (Table table : tables) {
            for (Column column : table.getColumns()) {
                try (Statement statement1 = connection.createStatement()) {
                    Map<String, List<String>> foreignKeys = new HashMap<>();

                    String query = "SELECT \n" +
                            "    confrelid::regclass AS referenced_table,\n" +
                            "    af.attname AS referenced_column\n" +
                            "FROM \n" +
                            "    pg_constraint\n" +
                            "JOIN \n" +
                            "    pg_attribute AS a ON a.attnum = ANY(conkey) AND a.attrelid = conrelid\n" +
                            "JOIN \n" +
                            "    pg_attribute AS af ON af.attnum = ANY(confkey) AND af.attrelid = confrelid\n" +
                            "WHERE \n" +
                            "    confrelid IS NOT NULL AND\n" +
                            "    a.attname = '" + column.getName() + "'";

                    try (ResultSet foreignKeysRow = statement1.executeQuery(query)) {
                        while (foreignKeysRow.next()) {
                            String referencedTable = foreignKeysRow
                                    .getObject(1)
                                    .toString()
                                    .replace("schema_" + schemaId + ".", "");

                            String referencedColumn = foreignKeysRow
                                    .getObject(2)
                                    .toString();

                            if (!referencedTable.equals(table.getName())) {
                                if (!foreignKeys.containsKey(referencedTable)) {
                                    foreignKeys.put(referencedTable, new ArrayList<>());
                                }
                                foreignKeys.get(referencedTable).add(referencedColumn);
                            }
                        }

                        if (!foreignKeys.isEmpty()) {
                            column.setKeyStatus(Column.KeyStatus.FOREIGN);
                        }

                        column.setForeignKeys(foreignKeys);
                    }
                }
            }
        }
    }

    @Transactional
    public Schema postgresToMongo(Schema schema) {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "1234")) {
            // Инициализируем новую схему
            String schemaId = schema.getId();
            Schema newSchema = new Schema();
            newSchema.setId(schemaId);
            newSchema.setUserId(schema.getUserId());
            newSchema.setName(schema.getName());

            // Заполняем массив таблиц
            try (Statement statement = connection.createStatement()) {
                List<Table> newTables = new ArrayList<>();

                try (ResultSet resultSet = statement.executeQuery("select table_name from information_schema.tables where table_schema = 'schema_" + schemaId + "'")) {
                    while (resultSet.next()) {
                        String tableName = resultSet.getString("table_name");
                        Table newTable = new Table();
                        newTable.setName(tableName);
                        newTable.setValues(new ArrayList<>());

                        // Вытаскиваем данные о записях таблицы
                        extractValuesFromTable(connection, schemaId, tableName, newTable);

                        // Вытаскиваем данные о колонках таблицы
                        List<Column> columns = new ArrayList<>();
                        extractColumnsFromTable(connection, schemaId, tableName, columns);

                        newTable.setColumns(columns);
                        newTables.add(newTable);
                    }

                    // Вытаскиваем данные о внешних ключах таблиц
                    extractForeignKeysFromTables(connection, schemaId, newTables);
                } finally {
                    newSchema.setTables(newTables);
                    return newSchema;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return schema;
        }
    }

    @Transactional
    public String dropSchema(String id) {
        // Заходим под пользователем с ограниченными правами
        try (Connection connection = DriverManager
                .getConnection(
                        "jdbc:postgresql://localhost:5432/test",
                        "postgres",
                        "1234"
                )
        ) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("drop schema schema_" + id + " cascade");
            }
            return "ok";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    @Transactional
    public String getQueryResult(String sqlQuery, Schema schema, String userName, String userPassword) {
        String schemaName = "schema_" + schema.getId();
        String intermediateResult;

        // 1. Миграция данных из MongoDB в Postgres
        intermediateResult = mongoToPostgres(schema);
        if (!intermediateResult.equals("ok")) {
            dropSchema(schema.getId());
            return intermediateResult;
        }

        // 2. Настраиваем привилегии для пользователя
        intermediateResult = createUser(userName, userPassword, schemaName);
        if (!intermediateResult.equals("ok")) {
            dropSchema(schema.getId());
            dropUser(userName);
            return intermediateResult;
        }

        // 3. Отправляет запрос пользователя
        String queryResult = executeSqlQuery(sqlQuery, userName, userPassword);
        if (queryResult == null) {
            queryResult = "В таблице отсутствуют записи.";
        }
        if (queryResult.toLowerCase().endsWith("запрос не вернул результатов.")) {
            queryResult = "Успешно: " + queryResult.toLowerCase();
        }

        return queryResult;
    }

    @Transactional
    public String executeUserSQL(String sqlQuery, Schema schema) {
        String userName = "user_" + schema.getUserId();
        String userPassword = schema.getId();

        // 1. Получаем результат запроса
        String queryResult = getQueryResult(sqlQuery, schema, userName, userPassword);

        // 2. Сохранение новой схемы
        Schema newSchema = postgresToMongo(schema);

        // 3. Обновление данных в MongoDB
        schemaRepository.save(newSchema);

        // 4. Закрываем сессию пользователя
        userSchemaRollback(newSchema.getId(), userName);

        return queryResult;
    }

    @Transactional
    public void userSchemaRollback(String schemaId, String username) {
        dropSchema(schemaId);
        dropUser(username);
    }

    @Transactional
    public String executeUserSQLWithoutSave(String sqlQuery, Schema schema) {
        String userName = "user_" + schema.getUserId();
        String userPassword = schema.getId();

        // 1. Получаем результат запроса
        String queryResult = getQueryResult(sqlQuery, schema, userName, userPassword);

        // 2. Закрываем сессию пользователя
        userSchemaRollback(schema.getId(), userName);

        return queryResult;
    }

    @Transactional
    public String executeSqlQuery(String sqlQuery, String userName, String userPassword) {
        try (Connection connection = DriverManager
                .getConnection(
                        "jdbc:postgresql://localhost:5432/test",
                        userName,
                        userPassword
                )
        ) {
            try (Statement statement = connection.createStatement()) {
                String query = "";
                if (sqlQuery.toLowerCase().startsWith("select")) {
                    query = "select json_agg(data) from (" + sqlQuery + ") as data";
                } else {
                    query = sqlQuery;
                }

                try {
                    ResultSet result = statement.executeQuery(query);
                    while (result.next()) {
                        return result.getString(1);
                    }
                    return "Успешно: Запрос не вернул результатов.";
                } catch (SQLException e) {
                    return e.getMessage();
                }
            }
        } catch (SQLException e) {
            return "Ошибка при выполнении SQL-запроса: " + e.getMessage();
        }
    }

    @Transactional
    public String createUser(String userName, String userPassword, String schemaName) {
        try (Connection connection = DriverManager
                .getConnection(
                        "jdbc:postgresql://localhost:5432/test",
                        "postgres",
                        "1234"
                )
        ) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("CREATE USER " + userName + " with password '" + userPassword + "'");
                statement.execute("REVOKE ALL PRIVILEGES ON DATABASE test FROM " + userName);
                statement.execute("ALTER SCHEMA " + schemaName + " OWNER TO " + userName);
                statement.execute("GRANT USAGE ON SCHEMA " + schemaName + " TO " + userName);
                statement.execute("GRANT ALL ON ALL TABLES IN SCHEMA " + schemaName + " TO " + userName);
                statement.execute("ALTER USER " + userName + " SET search_path = '" + schemaName + "'");
                statement.execute(
                            "DO $$ " +
                                "DECLARE " +
                                "   t_name text; " +
                                "BEGIN " +
                                "   FOR t_name IN (SELECT table_name FROM information_schema.tables " +
                                    "WHERE table_schema = '" + schemaName + "') " +
                                "   LOOP " +
                                "      EXECUTE 'ALTER TABLE " + schemaName + ".' || t_name || ' OWNER TO " + userName + "'; " +
                                "   END LOOP; " +
                                "END $$;"
                );

                return "ok";
            } catch (SQLException e) {
                return e.getMessage();
            }
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    @Transactional
    public String dropUser(String userName) {
        try (Connection connection = DriverManager
                .getConnection(
                        "jdbc:postgresql://localhost:5432/test",
                        "postgres",
                        "1234"
                )
        ) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP USER " + userName);
                return "ok";
            } catch (SQLException e) {
                return e.getMessage();
            }
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public void saveSchema(Schema schema) {
        schemaRepository.save(schema);
    }

    public void deleteSchema(Schema schema) {
        schemaRepository.delete(schema);
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public void deleteOnlineLink(OnlineLink onlineLink) {
        onlineLinkRepository.delete(onlineLink);
    }

    public void deleteOfflineLink(OfflineLink offlineLink) {
        offlineLinkRepository.delete(offlineLink);
    }

    public void saveOnlineLink(OnlineLink onlineLink) {
        onlineLinkRepository.save(onlineLink);
    }

    public OnlineLink getOnlineLinkBySchemaId(String schemaId) {
        return onlineLinkRepository.getOnlineLinkBySchemaId(schemaId);
    }

    public OnlineLink getOnlineLinkByCode(String code) {
        return onlineLinkRepository.getOnlineLinkByCode(code);
    }

    public OfflineLink getOfflineLinkByCode(String code) {
        return offlineLinkRepository.getOfflineLinkByCode(code);
    }

    public List<OfflineLink> getOfflineLinksBySchemaId(String schemaId) {
        return offlineLinkRepository.getOfflineLinksBySchemaId(schemaId);
    }

    public Task getTaskById(String taskId) {
        return taskRepository.getTaskById(taskId);
    }

    public void saveOfflineLink(OfflineLink offlineLink) {
        offlineLinkRepository.save(offlineLink);
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }
}