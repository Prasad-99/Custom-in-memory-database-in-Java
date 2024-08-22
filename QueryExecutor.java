import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QueryExecutor {
    private InMemoryDatabase database;

    public QueryExecutor(InMemoryDatabase database) {
        this.database = database;
    }

    public Object executeQuery(Query query){
        return switch (query.getOperationType()) {
            case SELECT -> executeSelect(query);
            case INSERT -> executeInsert(query);
            case UPDATE -> executeUpdate(query);
            case DELETE -> executeDelete(query);
        };
    }

    //DELETE FROM users WHERE name = 'Alice'
    private Object executeDelete(Query query) {
        for (UserProfile user : database.getAllUserProfiles()){
            if (matchesConditions(user, query.getWhereConditions(), query)){
                database.deleteUserProfile(user.getUserId());
            }
        }
        return "User Profile Deleted";
    }

    //UPDATE users SET email = 'newalice@example.com' WHERE name = 'Alice'
    private String executeUpdate(Query query) {
        for (UserProfile user : database.getAllUserProfiles()){
            if (matchesConditions(user, query.getWhereConditions(), query)){
                query.getUpdateValues().forEach((field, value) -> setFieldValue(user, field, value));
                database.updateUserProfile(user.getUserId(), user);
            }
        }
        return "User Profile Updated";
    }


    //INSERT INTO users (name, email, age) VALUES ('Alice', 'alice@example.com', 30)
    private String executeInsert(Query query) {
        UserProfile newUser = new UserProfile(
                Integer.parseInt((String) query.getInsertValues().get("age")),
                (String) query.getInsertValues().get("email"),
                (String) query.getInsertValues().get("name"),
                UUID.randomUUID().toString()
        );
        database.createUserProfile(newUser);
        return "User Profile inserted successfully";
    }

    //SELECT name, email FROM users WHERE age > 25 AND name = 'Alice' ORDER BY age DESC;
    private String executeSelect(Query query){
        List<UserProfile> results = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if (database.getAllUserProfiles() == null){
            System.out.println("No user profiles found");
            return null;
        }

        for (UserProfile user : database.getAllUserProfiles()){
            if (matchesConditions(user, query.getWhereConditions(), query)){
                for (int i = 0; i < query.getSelectFields().size(); i++) {
                    String fieldValue = (String) getFieldValue(user, query.getSelectFields().get(i));
                    sb.append(query.getSelectFields().get(i));
                    sb.append(": ");
                    sb.append(fieldValue);
                    sb.append("\n");
                }
                results.add(user);
            }
        }

        if (query.getOrderByField() != null){
            results.sort((u1, u2) -> {
                Comparable value1 = (Comparable) getFieldValue(u1, query.getOrderByField());
                Comparable value2 = (Comparable) getFieldValue(u2, query.getOrderByField());
                return query.isOrderByDescending() ? value2.compareTo(value1) : value1.compareTo(value2);
            });
        }

        if (results.isEmpty()){
            System.out.println("No user profiles found");
        }

        if (Objects.equals(query.getSelectFields().getFirst(), "*")) {
            return results.toString();
        }

        return sb.toString();
    }

    private Object getFieldValue(UserProfile user, String orderByField) {
        return switch (orderByField.toLowerCase()) {
            case "name" -> user.getName();
            case "age" -> String.valueOf(user.getAge());
            case "email" -> user.getEmail();
            default -> null;
        };
    }

    private void setFieldValue(UserProfile user, String field, Object value) {
        switch (field.toLowerCase()) {
            case "name" -> user.setName((String) value);
            case "age" -> user.setAge((Integer) value);
            case "email" -> user.setEmail((String) value);
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        }
    }


    private boolean matchesConditions(UserProfile user, List<Conditions> conditions, Query query){
        if (query.getOperationType() == OperationType.SELECT){
            if (Objects.equals(query.getSelectFields().getFirst(), "*") && query.getWhereConditions().isEmpty()){
                return true;
            }
            if (!Objects.equals(query.getSelectFields().getFirst(), "*") && !query.getSelectFields().isEmpty() && query.getWhereConditions().isEmpty()){
                return true;
            }
        }

        List<Integer> flag = new ArrayList<>();

        for (Conditions value : conditions) {
            if (value.matches(user)) {
                flag.add(1);
            } else flag.add(0);
        }

        int result = flag.getFirst();
        int i = 1;
        for (Conditions value : conditions){
            if (Objects.equals(value.getLogicalOperator(), "AND")){
                result = result & flag.get(i);
                i++;
            }
            if (Objects.equals(value.getLogicalOperator(), "OR")){
                result = result | flag.get(i);
                i++;
            }
        }
        return result == 1;
    }
}
