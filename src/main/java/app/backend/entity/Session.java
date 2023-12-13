package app.backend.entity;

import java.sql.DatabaseMetaData;
import java.sql.Statement;

public class Session {
    private Integer status;
    private Statement statement;
    private DatabaseMetaData metaData;

    public Session(Integer status, Statement statement, DatabaseMetaData metaData) {
        this.status = status;
        this.statement = statement;
        this.metaData = metaData;
    }

    public Statement getStatement() {
        return statement;
    }

    public DatabaseMetaData getMetaData() {
        return metaData;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
