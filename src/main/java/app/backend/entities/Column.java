package app.backend.entities;

public class Column {
    private String name;
    private String dataType;
    private boolean notNull;
    private boolean autoInc;
    private String defaultDefinition;

    public Column(String name, String dataType, boolean notNull, boolean autoInc, String defaultDefinition) {
        this.name = name;
        this.dataType = dataType;
        this.notNull = notNull;
        this.autoInc = autoInc;
        this.defaultDefinition = defaultDefinition;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public boolean isAutoInc() {
        return autoInc;
    }

    public String getDefaultDefinition() {
        return defaultDefinition;
    }
}
