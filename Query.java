import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {
    private OperationType operationType;
    private List<String> selectFields = new ArrayList<>();
    private String fromTable;
    private List<Conditions> whereConditions = new ArrayList<>();
    private String orderByField;
    private boolean orderByDescending;

    private Map<String, Object> insertValues = new HashMap<>();
    private Map<String, Object> updateValues = new HashMap<>();

    public String getFromTable() {
        return fromTable;
    }

    public void setFromTable(String fromTable) {
        this.fromTable = fromTable;
    }

    public Map<String, Object> getInsertValues() {
        return insertValues;
    }

    public void setInsertValues(Map<String, Object> insertValues) {
        this.insertValues = insertValues;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public boolean isOrderByDescending() {
        return orderByDescending;
    }

    public void setOrderByDescending(boolean orderByDescending) {
        this.orderByDescending = orderByDescending;
    }

    public String getOrderByField() {
        return orderByField;
    }

    public void setOrderByField(String orderByField) {
        this.orderByField = orderByField;
    }

    public List<String> getSelectFields() {
        return selectFields;
    }

    public void setSelectFields(List<String> selectFields) {
        this.selectFields = selectFields;
    }

    public Map<String, Object> getUpdateValues() {
        return updateValues;
    }

    public void setUpdateValues(Map<String, Object> updateValues) {
        this.updateValues = updateValues;
    }

    public List<Conditions> getWhereConditions() {
        return whereConditions;
    }

    public void setWhereConditions(List<Conditions> whereConditions) {
        this.whereConditions = whereConditions;
    }
}
