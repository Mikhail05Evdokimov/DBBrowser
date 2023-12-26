package app.backend.utility;

import java.util.List;

public class dbSpecificProps {
    public static List<String> getSQLiteProps() {
        return List.of("url");
    }

    public static List<String> getExtraSQLiteProps() {
        return List.of("path");
    }

    public static List<String> getPostgreSQLProps() {
        // maybe will be needed:
        // .setServerName
        // .setPortNumber
        // .setDatabaseName
        return List.of("url", "username", "password");
    }

    public static List<String> getExtraPostgreSQLProps() {
        return List.of("host", "port", "dbname");
    }
}
