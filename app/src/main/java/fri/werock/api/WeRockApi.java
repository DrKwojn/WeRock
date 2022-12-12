package fri.werock.api;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import fri.werock.R;
import fri.werock.models.AuthenticationToken;
import fri.werock.models.User;
import fri.werock.models.UserAccount;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
                    ApiError error = new ApiError();
                    ResponseBody responseBody = response.errorBody();
                    if(responseBody != null) {
                        String errorJson = null;
                        try {
                            errorJson = responseBody.string();
                        } catch (IOException ignored) {}

                        if(errorJson != null) {
                            callback.onError(error);
                        }
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

    @GET("valid")
    Call<Void> validate();

    @GET("user/list")
    Call<List<User>> getUserList();
}
