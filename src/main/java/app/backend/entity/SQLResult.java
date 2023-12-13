package app.backend.entity;

import java.util.List;

public class SQLResult {
    private SQLQuery relatedSQLQuery;
    private String resulMessage;
    private List<Row> data;

    public SQLResult(SQLQuery relatedSQLQuery, String resulMessage, List<Row> data) {
        this.relatedSQLQuery = relatedSQLQuery;
        this.resulMessage = resulMessage;
        this.data = data;
    }

    public String getResulMessage() {
        return resulMessage;
    }

    public void setResulMessage(String resulMessage) {
        this.resulMessage = resulMessage;
    }

    public List<Row> getData() {
        return data;
    }

    public void setData(List<Row> data) {
        this.data = data;
    }
}
