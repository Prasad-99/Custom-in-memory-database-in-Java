public class Conditions {
    private String field;
    private String operator;
    private String value;
    private String logicalOperator;

    public Conditions(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public Conditions(String field, String operator, String value, String logicalOperator) {
        this.field = field;
        this.logicalOperator = logicalOperator;
        this.operator = operator;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public boolean matches(UserProfile user) {
        Object fieldValue = getFieldValue(user, field);

        if (fieldValue == null) {
            return false;
        }

        switch (operator) {
            case "=":
                return fieldValue.toString().equals(value);
            case ">":
                if (fieldValue instanceof Comparable) {
                    return ((Comparable) fieldValue.toString()).compareTo(value) > 0;
                }
                break;
            case "<":
                if (fieldValue instanceof Comparable) {
                    return ((Comparable) fieldValue.toString()).compareTo(value) < 0;
                }
                break;
            case ">=":
                if (fieldValue instanceof Comparable) {
                    return ((Comparable) fieldValue.toString()).compareTo(value) >= 0;
                }
                break;
            case "<=":
                if (fieldValue instanceof Comparable) {
                    return ((Comparable) fieldValue.toString()).compareTo(value) <= 0;
                }
                break;
            case "!=":
                return !fieldValue.toString().equals(value);
            default:
                throw new UnsupportedOperationException("Unknown operator: " + operator);
        }

        return false;
    }

    private Object getFieldValue(UserProfile user, String fieldName) {
        return switch (fieldName.toLowerCase()) {
            case "name" -> user.getName();
            case "email" -> user.getEmail();
            case "age" -> user.getAge();
            default -> null;
        };
    }
}
