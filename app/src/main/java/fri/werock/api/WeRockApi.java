package fri.werock.api;

import android.content.Context;

import java.io.IOException;
import java.util.List;

import fri.werock.R;
import fri.werock.model.User;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WeRockApi {
    static WeRockApi create(Context context, String accessToken) {
        String serverUrl = context.getString(R.string.server_url);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            return chain.proceed(newRequest);
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WeRockApi.class);
    }

    @GET("user/list")
    Call<List<User>> getUserList();
}
