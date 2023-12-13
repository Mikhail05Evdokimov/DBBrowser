package app.backend.entity;

public class Column {
    private String name;
    private String dataType;
    private Boolean notNullConstraint;
    private Boolean autoIncrementConstraint;
    private String defaultConstraint;

    public Column(String name, String dataType, Boolean notNullConstraint, Boolean autoIncrementConstraint, String defaultConstraint) {
        this.name = name;
        this.dataType = dataType;
        this.notNullConstraint = notNullConstraint;
        this.autoIncrementConstraint = autoIncrementConstraint;
        this.defaultConstraint = defaultConstraint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getNotNullConstraint() {
        return notNullConstraint;
    }

    public void setNotNullConstraint(Boolean notNullConstraint) {
        this.notNullConstraint = notNullConstraint;
    }

    public Boolean getAutoIncrementConstraint() {
        return autoIncrementConstraint;
    }

    public void setAutoIncrementConstraint(Boolean autoIncrementConstraint) {
        this.autoIncrementConstraint = autoIncrementConstraint;
    }

    public String getDefaultConstraint() {
        return defaultConstraint;
    }

    public void setDefaultConstraint(String defaultConstraint) {
        this.defaultConstraint = defaultConstraint;
    }
}
