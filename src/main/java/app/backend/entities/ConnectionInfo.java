package app.backend.entities;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.backend.utility.dbSpecificProps;

public class ConnectionInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public enum ConnectionType {
        SQLITE,
        POSTGRESQL
    }

    private final ConnectionType connectionType;
    private final List<String> propertiesNames;
    private final Map<String, String> properties;

    public ConnectionInfo(ConnectionType connectionType, Map<String, String> info) {
        this.connectionType = connectionType;
        propertiesNames = new ArrayList<>();
        properties = new HashMap<>();
        switch (this.connectionType) {
            case SQLITE -> {
                propertiesNames.addAll(dbSpecificProps.getSQLiteProps());
                if (info.containsKey("path")) {
                    info.put("url", ConnectionInfo.filePathToUrlSQLite(info.get("path")));
                }
            }
            case POSTGRESQL -> {
                propertiesNames.addAll(dbSpecificProps.getPostgreSQLProps());
                if (info.containsKey("host") && info.containsKey("dbname")) {
                    info.put("url", ConnectionInfo.postgresSQLDataToUrl(
                            info.get("host"), info.get("port"), info.get("dbname")));
                }
            }
            default -> throw new InvalidParameterException("Invalid DB type");
        }
        for (String p : propertiesNames) {
            if (!info.containsKey(p)) {
                throw new InvalidParameterException("Not enough properties for connectionInfo creation");
            }
            properties.put(p, info.get(p));
        }
    }

    public static String filePathToUrlSQLite(String path) {
        return "jdbc:sqlite:" + path;
    }

    public static String postgresSQLDataToUrl(String host, String port, String dbName) {
        if (port != null) {
            return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
        } else {
            return "jdbc:postgresql://" + host + "/" + dbName;
        }
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public List<String> getPropertiesNames() {
        return propertiesNames;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "ConnectionInfo{" +
                "connectionType=" + connectionType +
                ", propertiesNames=" + propertiesNames +
                ", properties=" + properties +
                '}';
    }
}