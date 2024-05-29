package app.api;

import app.api.data.LogInData;
import app.widgets.dialogs.start.OnlineStartDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCalls {
    private static final Signaller signaller = new Signaller();

    private static final String backendUrl = "http://127.0.0.0:8080";

    public static void signIn(OnlineStartDialog controller, String login, String password) {

        var service = new Retrofit.Builder()
            .baseUrl(backendUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserInterface.class);

        LogInData data = new LogInData(login, password);

        service.login(data).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response.body().toString());
                if(response.code() == 200) {
                    UserDataRepository.userData = data;
                    signaller.emitSignal(controller, "0");
                }
                else {
                    signaller.emitSignal(controller, "1");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.toString());
                signaller.emitSignal(controller, "2");
            }
        });
    }

}
