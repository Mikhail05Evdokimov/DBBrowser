package app.backend.entity;

import java.net.URL;

public class ConnectionInfo {
    private String type;
    private URL url;

    public ConnectionInfo(String type, URL url) {
        this.type = type;
        this.url = url;
    }
}
