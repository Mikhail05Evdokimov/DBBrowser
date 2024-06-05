package app.api;

import app.api.data.ItemResponse;
import app.api.data.LogInData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserInterface {

    @POST("auth/sign-in")
    Call<ResponseBody> login(@Body LogInData data);

    @GET("api/")
    Call<ResponseBody> getUser();

    @GET("/items")
    Call<ItemResponse> getItems();

}
