package app.api;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiCalls {

    public static boolean signIn(String login, String password) {

        try {

            JSONObject rowBody = new JSONObject();
            rowBody.put("login", login);
            rowBody.put("password", password);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.example.com/page.php"))
                .POST(HttpRequest.BodyPublishers.ofString(rowBody.toString()))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject responseBody = new JSONObject(response.body());
            System.out.println(responseBody);

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        return true;

    }

}
