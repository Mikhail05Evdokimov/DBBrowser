package app.api;

import app.api.data.ItemResponse;
import app.api.data.LogInData;
import app.widgets.dialogs.start.OnlineStartDialog;
import io.qt.core.QObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ApiCalls {
    private static final Signaller signaller = new Signaller();

    private static final String backendUrl = "http://127.0.0.0:8080";
    private static UserInterface userService = null;

    private static UserInterface getUserService() {
        if (userService == null) {
            userService = new Retrofit.Builder()
                .baseUrl(backendUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserInterface.class);
        }
        return userService;
    }

    public static void signIn(QObject.Signal1<String> signal1, String login, String password) {

        var service = getUserService();

        LogInData data = new LogInData(login, password);

        service.login(data).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.body().toString());
                if(response.code() == 200) {
                    UserDataRepository.userData = data;
                    signal1.emit("0");
                }
                else {
                    signal1.emit("1");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.toString());
                signal1.emit("2");
            }
        });
    }

    public static void getUser(QObject.Signal1<String> signal1) {

        var service = new Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserInterface.class);

        service.getUser().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.body().toString());
                if(response.code() == 200) {
                    try {
                        signal1.emit(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    signal1.emit("ERROR");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.toString());
                signal1.emit("ERROR");
            }
        });
    }

    public static void getItems(QObject.Signal1<String> signal1) {

        var service = new Retrofit.Builder()
            .baseUrl("http://79.137.205.181:8081")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserInterface.class);

        service.getItems().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                System.out.println(response.body().toString());
                if(response.code() == 200) {
                    signal1.emit(response.body().payload.rows.get(0).name);
                }
                else {
                    signal1.emit("ERROR");
                }
            }

            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                System.out.println(t.toString());
                signal1.emit("ERROR");
            }
        });
    }

}
