import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
    public Query parse(String queryString) {
        Query query = new Query();

        // Regex pattern to match tokens, including those within brackets
        Pattern pattern = Pattern.compile("[^\\s()]+|\\([^)]*\\)");

        Matcher matcher = pattern.matcher(queryString);

        List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        int index = 0;
        String firstToken = tokens.get(index).toUpperCase();
        switch (firstToken) {
            case "SELECT":
                query.setOperationType(OperationType.SELECT);
                parseSelect(query, tokens, index + 1);
                break;
            case "INSERT":
                query.setOperationType(OperationType.INSERT);
                parseInsert(query, tokens, index + 1);
                break;
            case "UPDATE":
                query.setOperationType(OperationType.UPDATE);
                parseUpdate(query, tokens, index + 1);
                break;
            case "DELETE":
                query.setOperationType(OperationType.DELETE);
                parseDelete(query, tokens, index + 1);
                break;
            default:
                throw new IllegalArgumentException("Unknown operation type: " + firstToken);
        }
        return query;
    }

    //SELECT name, email FROM users WHERE age > 25 AND name = 'Alice' ORDER BY age DESC;
    private void parseSelect(Query query, List<String> tokens, int startIndex) {
        int index = startIndex;

        while (!tokens.get(index).equalsIgnoreCase("FROM")) {
            query.getSelectFields().add(tokens.get(index).replace(",", ""));
            index++;
        }
        index++;
        query.setFromTable(tokens.get(index));

        index++;
        if (index < tokens.size() && tokens.get(index).equalsIgnoreCase("WHERE")) {
            index++;
            parseConditions(query, tokens, index);
        }

        if (index < tokens.size() && tokens.get(index).equalsIgnoreCase("ORDER")) {
            index++;
            if (tokens.get(index).equalsIgnoreCase("BY")) {
                index++;
                query.setOrderByField(tokens.get(index));
                index++;
                if (index < tokens.size() && tokens.get(index).equalsIgnoreCase("DESC")) {
                    query.setOrderByDescending(true);
                } else {
                    query.setOrderByDescending(false);
                }
            }
        }
    }

    //INSERT INTO users (name, email, age) VALUES ('Alice', 'alice@example.com', 30)
    private void parseInsert(Query query, List<String> tokens, int startIndex) {
        int index = startIndex;

        if (tokens.get(index).equalsIgnoreCase("INTO")) {
            index++;
            query.setFromTable(tokens.get(index));

            index++;
            if (tokens.get(index).startsWith("(")) {
                String[] fields = tokens.get(index).replace("(","").replace(")","").split(",");
                index +=2;

                String[] values = tokens.get(index).replace("(","").replace(")","").split(",");
                for (int i = 0; i < fields.length; i++) {
                    query.getInsertValues().put(fields[i].trim(), values[i].trim());
                }
            }
        }
    }

    //UPDATE users SET email = 'newalice@example.com' WHERE name = 'Alice'
    private void parseUpdate(Query query, List<String> tokens, int startIndex) {
        int index = startIndex;

        query.setFromTable(tokens.get(index));
        index++;

        if (tokens.get(index).equalsIgnoreCase("SET")) {
            index++;
            while (!tokens.get(index).equalsIgnoreCase("WHERE")) {
                String field = tokens.get(index);
                index+=2;
                String value = tokens.get(index).replace(",", "").replace("'","");
                query.getUpdateValues().put(field,value);
                index++;
            }
        }

        index++;
        parseConditions(query, tokens, index);
    }

    //DELETE FROM users WHERE name = 'Alice'
    private void parseDelete(Query query, List<String> tokens, int startIndex){
        int index = startIndex;

        if (tokens.get(index).equalsIgnoreCase("FROM")){
            index++;
            query.setFromTable(tokens.get(index));
            index++;

            if (index < tokens.size() && tokens.get(index).equalsIgnoreCase("WHERE")){
                index++;
                parseConditions(query, tokens, index);
            }
        }
    }

    private void parseConditions(Query query, List<String> tokens, int startIndex){
        int index = startIndex;

        while (index < tokens.size() && !tokens.get(index).equalsIgnoreCase("ORDER")){
            String field = tokens.get(index);
            index++;
            String operator = tokens.get(index);
            index++;
            String value = tokens.get(index).replace("'","");
            index++;

            query.getWhereConditions().add(new Conditions(field, operator, value));

            if (index < tokens.size() && (tokens.get(index).equalsIgnoreCase("AND") || tokens.get(index).equalsIgnoreCase("OR"))){
                query.getWhereConditions().removeLast();
                query.getWhereConditions().add(new Conditions(field, operator, value, tokens.get(index)));
                index++;
            }
        }
    }
}
