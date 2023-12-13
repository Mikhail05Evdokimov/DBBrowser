package app.backend.entity;

public class SQLQuery {
    private String query;
    private Conection relatedConnection;

    public SQLQuery(String query, Conection relatedConnection) {
        this.query = query;
        this.relatedConnection = relatedConnection;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
