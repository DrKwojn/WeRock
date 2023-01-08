package fri.werock.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fri.werock.R;
import fri.werock.models.AuthenticationToken;
import fri.werock.models.User;
import fri.werock.models.UserAccount;
import okhttp3.ConnectionPool;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface WeRockApi {
    static WeRockApi create(Context context) {
        return WeRockApi.create(context, null);
    }

    static WeRockApi create(Context context, String accessToken) {
        String serverUrl = context.getString(R.string.server_url);
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        if(accessToken != null) {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.addHeader("Authorization", "Bearer " + accessToken);
                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            }).build();
            retrofitBuilder.client(client);
        }

        retrofitBuilder.baseUrl(serverUrl);
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(WeRockApi.class);
    }

    static <T> void fetch(Call<T> call, WeRockApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if(response.isSuccessful()) {
                    callback.onResponse(response.body());
                } else {
                    ResponseBody responseBody = response.errorBody();
                    if(responseBody == null) {
                        callback.onError(WeRockApiError.BODY_MISSING_ERROR);
                        return;
                    }

                    String errorJson = null;
                    try {
                        errorJson = responseBody.string();
                    } catch (IOException ignored) {}

                    if(errorJson == null || errorJson.isEmpty()) {
                        callback.onError(WeRockApiError.BODY_MISSING_ERROR);
                        return;
                    }

                    WeRockApiError error;
                    try {
                        error = new Gson().fromJson(errorJson, WeRockApiError.class);
                    } catch (JsonSyntaxException exception) {
                        error = WeRockApiError.FAILED_PARSING_JSON_ERROR;
                    }

                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @POST("register")
    Call<Void> register(@Body UserAccount userAccount);

    @POST("login")
    Call<AuthenticationToken> login(@Body UserAccount userAccount);

    @GET("user/list")
    Call<List<User>> getUsers();

    @GET("user/{id}")
    Call<User> getUser(@Path("id") int id);

    @PUT("user/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body User user);

    @GET("image/download")
    Call<ResponseBody> downloadImage();

    @Multipart
    @POST("image/upload")
    Call<Void> uploadImage(@Part MultipartBody.Part image);
}
